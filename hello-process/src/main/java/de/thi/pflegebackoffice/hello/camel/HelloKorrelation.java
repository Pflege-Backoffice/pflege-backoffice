/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.camel;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.hello.cep.KeineAntwortErkennung;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.cibseven.bpm.engine.RuntimeService;
import org.jboss.logging.Logger;

/**
 * Korrelation (Punkt 4): ordnet eine Backend-Antwort ueber den Business Key
 * (= Vorgangskennung) der wartenden Prozessinstanz zu.
 */
@ApplicationScoped
public class HelloKorrelation {

    private static final Logger LOG = Logger.getLogger(HelloKorrelation.class);

    @Inject
    RuntimeService runtimeService;

    @Inject
    KeineAntwortErkennung keineAntwortErkennung;

    /** Wirft MismatchingMessageCorrelationException, wenn kein Prozess auf diese Antwort wartet. */
    public void korrelieren(Umschlag<?> antwort) {
        String vorgangskennung = antwort.vorgangskennung();
        Object text = antwort.nutzdatenFeld("text");

        runtimeService.createMessageCorrelation(HelloKonstanten.NACHRICHT_BACKEND_ANTWORT)
                .processInstanceBusinessKey(vorgangskennung)
                .setVariable("antwort", text == null ? null : text.toString())
                .correlateWithResult();

        keineAntwortErkennung.antwortErhalten(vorgangskennung);
        LOG.infof("Backend-Antwort fuer Vorgang %s korreliert", vorgangskennung);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
