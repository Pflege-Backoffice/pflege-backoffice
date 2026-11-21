/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.prozess;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.hello.api.HelloStartDaten;
import de.thi.pflegebackoffice.hello.api.HelloStatusAntwort;
import de.thi.pflegebackoffice.hello.domain.HelloStatus;
import de.thi.pflegebackoffice.hello.domain.HelloVorgang;
import de.thi.pflegebackoffice.shared.fehler.FachlicherFehler;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.cibseven.bpm.engine.RuntimeService;
import org.cibseven.bpm.engine.runtime.ProcessInstance;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Fachlogik des Hello-Vorgangs: Prozess starten (Punkt 5), Status abfragen (Punkt 5),
 * Status in MySQL fortschreiben (Punkt 6).
 * abschliessen/keineAntwort werden von den Delegates der Service-Tasks aufgerufen.
 */
@ApplicationScoped
public class HelloVorgangDienst {

    @Inject
    RuntimeService runtimeService;

    @Transactional
    public HelloStatusAntwort starten(Umschlag<HelloStartDaten> anfrage) {
        String vorgangskennung = anfrage.vorgangskennung();
        if (HelloVorgang.findeMitKennung(vorgangskennung).isPresent()) {
            throw FachlicherFehler.konflikt("Vorgang " + vorgangskennung + " existiert bereits.");
        }

        HelloVorgang vorgang = new HelloVorgang();
        vorgang.vorgangskennung = vorgangskennung;
        vorgang.pflegehaushaltskennung = anfrage.pflegehaushaltskennung();
        vorgang.text = anfrage.nutzdaten().text();
        vorgang.kanal = anfrage.nutzdaten().kanal();
        vorgang.status = HelloStatus.WARTET_AUF_ANTWORT;
        vorgang.erstelltAm = Instant.now();
        vorgang.geaendertAm = vorgang.erstelltAm;
        vorgang.persist();

        // Business Key = Vorgangskennung: daran werden spaeter Nachrichten korreliert.
        runtimeService.startProcessInstanceByKey(HelloKonstanten.PROZESS_KEY, vorgangskennung, Map.of(
                "pflegehaushaltskennung", anfrage.pflegehaushaltskennung(),
                "text", vorgang.text,
                "kanal", vorgang.kanal));

        return status(vorgangskennung);
    }

    public HelloStatusAntwort status(String vorgangskennung) {
        HelloVorgang vorgang = HelloVorgang.findeMitKennung(vorgangskennung)
                .orElseThrow(() -> FachlicherFehler.nichtGefunden("Vorgang " + vorgangskennung + " existiert nicht."));

        ProcessInstance instanz = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(HelloKonstanten.PROZESS_KEY)
                .processInstanceBusinessKey(vorgangskennung)
                .singleResult();
        List<String> aktiveSchritte = instanz == null ? List.of() : runtimeService.getActiveActivityIds(instanz.getId());

        return new HelloStatusAntwort(vorgang.vorgangskennung, vorgang.pflegehaushaltskennung, vorgang.status,
                vorgang.antwort, instanz != null, aktiveSchritte);
    }

    /**
     * Prueft vor dem Einreihen einer Backend-Antwort, ob sie ueberhaupt zugeordnet werden kann:
     * 404, wenn der Vorgang unbekannt ist; 409, wenn er nicht (mehr) auf eine Antwort wartet.
     * <p>
     * Das ist eine Vorabpruefung fuer schnelle Rueckmeldung. Die eigentliche Zuordnung passiert
     * danach asynchron; laeuft der Vorgang genau dazwischen ab, landet die Antwort weiterhin in
     * hello.antwort.fehler.
     */
    public void pruefeWartetAufAntwort(String vorgangskennung) {
        HelloVorgang vorgang = HelloVorgang.findeMitKennung(vorgangskennung)
                .orElseThrow(() -> FachlicherFehler.nichtGefunden("Vorgang " + vorgangskennung + " existiert nicht."));
        if (vorgang.status != HelloStatus.WARTET_AUF_ANTWORT) {
            throw FachlicherFehler.konflikt("Vorgang " + vorgangskennung + " wartet auf keine Antwort mehr (Status "
                    + vorgang.status + ").");
        }
    }

    @Transactional
    public void abschliessen(String vorgangskennung, String antwort) {
        HelloVorgang vorgang = HelloVorgang.findeMitKennung(vorgangskennung).orElseThrow();
        vorgang.status = HelloStatus.ABGESCHLOSSEN;
        vorgang.antwort = antwort;
        vorgang.geaendertAm = Instant.now();
    }

    @Transactional
    public void keineAntwort(String vorgangskennung) {
        HelloVorgang vorgang = HelloVorgang.findeMitKennung(vorgangskennung).orElseThrow();
        vorgang.status = HelloStatus.KEINE_ANTWORT;
        vorgang.geaendertAm = Instant.now();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
