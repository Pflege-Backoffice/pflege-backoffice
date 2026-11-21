/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.ausgangspostfach;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.List;

/**
 * Versendet die committeten Eintraege des Ausgangspostfachs. Ein Camel-Timer ruft
 * {@link #versendeOffene()} regelmaessig auf (siehe {@link Route}).
 * <p>
 * Regeln:
 * <ul>
 *   <li>Reihenfolge nach Ablage (id). Schlaegt ein Versand fehl, wird der Durchlauf abgebrochen
 *       und spaeter wiederholt, damit spaetere Nachrichten nicht ueberholen.</li>
 *   <li>Nach {@code ausgangspostfach.max-versuche} Fehlversuchen bleibt ein Eintrag liegen
 *       (Log-Level ERROR, Spalte {@code letzterFehler}) und blockiert die anderen nicht mehr.</li>
 *   <li>{@code FOR UPDATE SKIP LOCKED}: laufen mehrere Instanzen der Anwendung, versendet jede
 *       andere Eintraege.</li>
 * </ul>
 */
@ApplicationScoped
public class AusgangspostfachVersand {

    private static final Logger LOG = Logger.getLogger(AusgangspostfachVersand.class);

    /** JPA-Hinweis "lock.timeout = -2" bedeutet bei Hibernate SKIP LOCKED. */
    private static final int SKIP_LOCKED = -2;

    @Inject
    ProducerTemplate camel;

    @Inject
    Event<NachrichtVersendet> versendet;

    @ConfigProperty(name = "ausgangspostfach.stapelgroesse", defaultValue = "50")
    int stapelgroesse;

    @ConfigProperty(name = "ausgangspostfach.max-versuche", defaultValue = "10")
    int maxVersuche;

    /** @return Anzahl der in diesem Durchlauf versendeten Nachrichten */
    @Transactional
    public int versendeOffene() {
        List<AusgangspostfachEintrag> offene = AusgangspostfachEintrag
                .find("gesendetAm is null and fehlversuche < ?1 order by id", maxVersuche)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .withHint("jakarta.persistence.lock.timeout", SKIP_LOCKED)
                .page(0, stapelgroesse)
                .list();

        int anzahl = 0;
        for (AusgangspostfachEintrag eintrag : offene) {
            try {
                camel.sendBody(eintrag.ziel, eintrag.inhalt);
            } catch (RuntimeException fehler) {
                vermerkeFehler(eintrag, fehler);
                break;
            }
            eintrag.gesendetAm = Instant.now();
            anzahl++;
            benachrichtigen(eintrag);
        }
        return anzahl;
    }

    private void vermerkeFehler(AusgangspostfachEintrag eintrag, RuntimeException fehler) {
        eintrag.fehlversuche++;
        String text = fehler.getClass().getSimpleName() + ": " + fehler.getMessage();
        eintrag.letzterFehler = text.length() > 1000 ? text.substring(0, 1000) : text;
        if (eintrag.fehlversuche >= maxVersuche) {
            LOG.errorf(fehler, "Ausgangspostfach: Eintrag %d an %s (Vorgang %s) nach %d Versuchen aufgegeben",
                    eintrag.id, eintrag.ziel, eintrag.vorgangskennung, eintrag.fehlversuche);
        } else {
            LOG.warnf("Ausgangspostfach: Versand von Eintrag %d an %s fehlgeschlagen (Versuch %d/%d): %s",
                    eintrag.id, eintrag.ziel, eintrag.fehlversuche, maxVersuche, text);
        }
    }

    /** Fehler eines Beobachters duerfen den Versand nicht zurueckrollen (sonst Duplikate). */
    private void benachrichtigen(AusgangspostfachEintrag eintrag) {
        try {
            versendet.fire(new NachrichtVersendet(eintrag.ziel, eintrag.vorgangskennung));
        } catch (RuntimeException fehler) {
            LOG.errorf(fehler, "Ausgangspostfach: Beobachter fuer Eintrag %d fehlgeschlagen", eintrag.id);
        }
    }

    /** Camel-Timer, der {@link #versendeOffene()} regelmaessig aufruft (nie parallel). */
    @ApplicationScoped
    public static class Route extends RouteBuilder {

        /** Die CDI-Bean (nicht die Klasse!) uebergeben, sonst fehlen Injektion und @Transactional. */
        @Inject
        AusgangspostfachVersand versand;

        @Override
        public void configure() {
            from("timer:ausgangspostfach?period={{ausgangspostfach.intervall-ms:500}}&delay=1000")
                    .routeId("ausgangspostfach-versand")
                    .bean(versand, "versendeOffene");
        }
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
