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

/**
 * Service-Task "Vorgang abschliessen": schreibt Status und Antwort nach MySQL (Punkt 6).
 * <p>
 * Hinweis: Unter Quarkus funktionieren im BPMN nur Bean-Namen wie {@code ${vorgangAbschliessenDelegate}},
 * KEINE Methodenaufrufe wie {@code ${dienst.methode(x)}}. Deshalb je Service-Task ein kleiner Delegate.
 */
@Named("vorgangAbschliessenDelegate")
@Dependent
public class VorgangAbschliessenDelegate implements JavaDelegate {

    @Inject
    HelloVorgangDienst dienst;

    @Override
    public void execute(DelegateExecution execution) {
        dienst.abschliessen(execution.getBusinessKey(), (String) execution.getVariable("antwort"));
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
