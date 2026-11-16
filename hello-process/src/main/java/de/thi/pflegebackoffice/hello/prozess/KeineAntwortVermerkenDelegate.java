/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.prozess;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cibseven.bpm.engine.delegate.DelegateExecution;
import org.cibseven.bpm.engine.delegate.JavaDelegate;

/** Service-Task "Keine Antwort vermerken": Esper hat eine Zeitueberschreitung gemeldet (Punkt 7). */
@Named("keineAntwortVermerkenDelegate")
@Dependent
public class KeineAntwortVermerkenDelegate implements JavaDelegate {

    @Inject
    HelloVorgangDienst dienst;

    @Override
    public void execute(DelegateExecution execution) {
        dienst.keineAntwort(execution.getBusinessKey());
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
