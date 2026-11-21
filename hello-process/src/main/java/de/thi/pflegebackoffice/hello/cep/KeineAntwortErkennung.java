/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.cep;

import de.thi.pflegebackoffice.cep.EsperDienst;
import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.shared.ausgangspostfach.NachrichtVersendet;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.cibseven.bpm.engine.RuntimeService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * Esper-Beispiel (Punkt 7): erkennt, dass auf eine Anfrage innerhalb eines Zeitfensters
 * KEINE Antwort kam, und meldet das als BPMN-Nachricht an den wartenden Prozess zurueck.
 */
@ApplicationScoped
public class KeineAntwortErkennung {

    private static final Logger LOG = Logger.getLogger(KeineAntwortErkennung.class);

    /**
     * EPL-Regel: "Fuer JEDE gesendete Anfrage a: wenn das Zeitfenster ablaeuft und
     * bis dahin KEINE Antwort mit derselben Vorgangskennung kam, melde a."
     * <p>
     * {@code @public @buseventtype}: der Ereignistyp darf von aussen (aus Java) gesendet werden.
     */
    private static final String EPL = """
            @public @buseventtype create schema AnfrageGesendet(vorgangskennung string);
            @public @buseventtype create schema AntwortErhalten(vorgangskennung string);

            @name('keine-antwort')
            select a.vorgangskennung as vorgangskennung
            from pattern [ every a=AnfrageGesendet
                           -> (timer:interval(%d sec)
                               and not AntwortErhalten(vorgangskennung = a.vorgangskennung)) ];
            """;

    @Inject
    EsperDienst esper;

    @Inject
    RuntimeService runtimeService;

    @ConfigProperty(name = "hello.esper.timeout-sekunden", defaultValue = "120")
    int timeoutSekunden;

    void beimStart(@Observes StartupEvent ereignis) {
        esper.regelInstallieren(EPL.formatted(timeoutSekunden), "keine-antwort", this::keineAntwort);
    }

    /**
     * Zeitfenster starten, sobald die Anfrage das System WIRKLICH verlassen hat
     * (Ausgangspostfach, also nach dem Commit) – nicht schon beim Ablegen.
     */
    void beiVersand(@Observes NachrichtVersendet ereignis) {
        if (HelloKonstanten.QUEUE_ANFRAGE.equals(ereignis.ziel())) {
            anfrageGesendet(ereignis.vorgangskennung());
        }
    }

    public void anfrageGesendet(String vorgangskennung) {
        esper.ereignisSenden("AnfrageGesendet", Map.of("vorgangskennung", vorgangskennung));
    }

    public void antwortErhalten(String vorgangskennung) {
        esper.ereignisSenden("AntwortErhalten", Map.of("vorgangskennung", vorgangskennung));
    }

    /** Rueckkopplung in den Prozess: BPMN-Nachricht "HelloKeineAntwort" korrelieren. */
    private void keineAntwort(Map<String, Object> ergebnis) {
        String vorgangskennung = (String) ergebnis.get("vorgangskennung");
        int anzahl = runtimeService.createMessageCorrelation(HelloKonstanten.NACHRICHT_KEINE_ANTWORT)
                .processInstanceBusinessKey(vorgangskennung)
                .correlateAllWithResult()
                .size();
        LOG.infof("Esper: keine Antwort fuer Vorgang %s nach %d s – %d Prozessinstanz(en) benachrichtigt",
                vorgangskennung, timeoutSekunden, anzahl);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
