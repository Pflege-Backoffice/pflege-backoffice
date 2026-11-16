/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.prozess;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.hello.cep.KeineAntwortErkennung;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import de.thi.pflegebackoffice.shared.nachricht.UmschlagFormate;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.ProducerTemplate;
import org.cibseven.bpm.engine.delegate.DelegateExecution;
import org.cibseven.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * BPMN-Sendeaufgabe "Anfrage an Backend senden" (Punkt 1):
 * legt eine JSON-Nachricht asynchron auf die JMS-Queue hello.anfrage.
 * Im BPMN referenziert als {@code camunda:delegateExpression="${anfrageSendenDelegate}"}.
 */
@Named("anfrageSendenDelegate")
@Dependent
public class AnfrageSendenDelegate implements JavaDelegate {

    @Inject
    ProducerTemplate camel;

    @Inject
    KeineAntwortErkennung keineAntwortErkennung;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String vorgangskennung = execution.getBusinessKey();

        Umschlag<Map<String, Object>> anfrage = Umschlag.neu(
                vorgangskennung,
                (String) execution.getVariable("pflegehaushaltskennung"),
                HelloKonstanten.ABSENDER,
                Map.of("text", execution.getVariable("text"),
                        "kanal", execution.getVariable("kanal")));

        // Senden ist asynchron: der Prozess wartet nicht auf die Verarbeitung der Nachricht.
        camel.sendBody(HelloKonstanten.QUEUE_ANFRAGE, UmschlagFormate.JSON_MAPPER.writeValueAsString(anfrage));

        // Esper beginnt jetzt, auf die passende Antwort zu warten (Punkt 7).
        keineAntwortErkennung.anfrageGesendet(vorgangskennung);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
