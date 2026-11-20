/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.app;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.hello.api.HelloStartDaten;
import de.thi.pflegebackoffice.hello.domain.HelloAusgangsnachricht;
import de.thi.pflegebackoffice.hello.domain.HelloVorgang;
import de.thi.pflegebackoffice.hello.prozess.HelloVorgangDienst;
import de.thi.pflegebackoffice.shared.ausgangspostfach.Ausgangspostfach;
import de.thi.pflegebackoffice.shared.ausgangspostfach.AusgangspostfachEintrag;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.narayana.jta.QuarkusTransactionException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionalException;
import org.cibseven.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ausgangspostfach (Transactional Outbox): Nachrichten gehen nur nach einem Commit raus,
 * fehlerhafte Eintraege werden wiederholt und blockieren nach max-versuche (Test: 3) nicht mehr.
 */
@QuarkusTest
class AusgangspostfachTest {

    private static final Duration MAX_WARTEZEIT = Duration.ofSeconds(20);
    private static final String ZIEL_OK = "bean:ausgangspostfachTestEmpfaenger?method=empfangen";
    /** Bean existiert nicht: jeder Versand wirft eine Ausnahme. */
    private static final String ZIEL_KAPUTT = "bean:gibtEsNicht";

    static final AtomicInteger EMPFANGEN = new AtomicInteger();

    @Inject
    Ausgangspostfach ausgangspostfach;

    @Inject
    HelloVorgangDienst dienst;

    @Inject
    RuntimeService runtimeService;

    @Test
    void ablegenOhneTransaktion_wirdAbgelehnt() {
        assertThrows(TransactionalException.class, () -> ausgangspostfach.ablegen(ZIEL_OK, "V-X", "{}"));
    }

    @Test
    void prozessstart_eintragWirdNachDemCommitVersendet() {
        String vk = neueVorgangskennung();
        QuarkusTransaction.requiringNew().run(() -> dienst.starten(startUmschlag(vk)));

        await().atMost(MAX_WARTEZEIT).untilAsserted(() -> {
            List<AusgangspostfachEintrag> eintraege = eintraege(vk);
            assertEquals(1, eintraege.size());
            assertEquals(HelloKonstanten.QUEUE_ANFRAGE, eintraege.get(0).ziel);
            assertNotNull(eintraege.get(0).gesendetAm);
        });
        await().atMost(MAX_WARTEZEIT).until(() -> anzahlAusgangsnachrichten(vk) == 1);
    }

    @Test
    void prozessstartMitRollback_nichtsWirdGesendet() throws InterruptedException {
        String vk = neueVorgangskennung();
        // simuliert einen Fehler NACH der Sendeaufgabe, z. B. beim Commit
        assertThrows(QuarkusTransactionException.class, () -> QuarkusTransaction.requiringNew().run(() -> {
            dienst.starten(startUmschlag(vk));
            QuarkusTransaction.setRollbackOnly();
        }));

        // Vorgang, Prozessinstanz UND Nachricht sind gemeinsam zurueckgerollt
        assertTrue(eintraege(vk).isEmpty());
        assertTrue(QuarkusTransaction.requiringNew().call(() -> HelloVorgang.findeMitKennung(vk).isEmpty()));
        assertEquals(0, runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(vk).count());

        // mehrere Versanddurchlaeufe abwarten: es darf nichts beim Backend ankommen
        Thread.sleep(1500);
        assertEquals(0, anzahlAusgangsnachrichten(vk));
    }

    @Test
    void versandFehler_wirdWiederholtUndBlockiertNachMaxVersuchenNicht() {
        String vk = neueVorgangskennung();
        int vorher = EMPFANGEN.get();
        QuarkusTransaction.requiringNew().run(() -> {
            ausgangspostfach.ablegen(ZIEL_KAPUTT, vk, "kaputt");
            ausgangspostfach.ablegen(ZIEL_OK, vk, "ok");
        });

        await().atMost(MAX_WARTEZEIT).untilAsserted(() -> {
            List<AusgangspostfachEintrag> eintraege = eintraege(vk);
            AusgangspostfachEintrag kaputt = eintraege.get(0);
            AusgangspostfachEintrag ok = eintraege.get(1);
            assertEquals(3, kaputt.fehlversuche);
            assertNull(kaputt.gesendetAm);
            assertNotNull(kaputt.letzterFehler);
            assertNotNull(ok.gesendetAm);
        });
        assertEquals(vorher + 1, EMPFANGEN.get());
    }

    private static List<AusgangspostfachEintrag> eintraege(String vk) {
        return QuarkusTransaction.requiringNew().call(() -> AusgangspostfachEintrag.fuerVorgang(vk));
    }

    private static long anzahlAusgangsnachrichten(String vk) {
        return QuarkusTransaction.requiringNew().call(() -> HelloAusgangsnachricht.anzahlFuer(vk));
    }

    private static Umschlag<HelloStartDaten> startUmschlag(String vk) {
        return Umschlag.neu(vk, "H-TEST-0001", "test", new HelloStartDaten("Hallo Backend", "DATENBANK"));
    }

    private static String neueVorgangskennung() {
        return "V-TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /** Test-Empfaenger fuer ZIEL_OK. */
    @ApplicationScoped
    @Named("ausgangspostfachTestEmpfaenger")
    public static class TestEmpfaenger {
        public void empfangen(String inhalt) {
            EMPFANGEN.incrementAndGet();
        }
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
