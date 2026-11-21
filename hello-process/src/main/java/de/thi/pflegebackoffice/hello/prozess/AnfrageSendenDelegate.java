/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.prozess;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.shared.ausgangspostfach.Ausgangspostfach;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cibseven.bpm.engine.delegate.DelegateExecution;
import org.cibseven.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * BPMN-Sendeaufgabe "Anfrage an Backend senden" (Punkt 1):
 * legt eine JSON-Nachricht fuer die JMS-Queue hello.anfrage ins Ausgangspostfach.
 * Im BPMN referenziert als {@code camunda:delegateExpression="${anfrageSendenDelegate}"}.
 * <p>
 * Nicht direkt senden! Die Nachricht darf erst raus, wenn die Transaktion committet ist, in der
 * der Prozess am Ereignis-Gateway gespeichert wird – sonst kann eine schnelle Antwort keinen
 * wartenden Prozess finden. Das Ausgangspostfach erledigt das (siehe {@link Ausgangspostfach}).
 * Die Esper-Zeitueberwachung startet, sobald die Nachricht wirklich versendet wurde
 * (siehe KeineAntwortErkennung#beiVersand).
 */
@Named("anfrageSendenDelegate")
@Dependent
public class AnfrageSendenDelegate implements JavaDelegate {

    @Inject
    Ausgangspostfach ausgangspostfach;

    @Override
    public void execute(DelegateExecution execution) {
        Umschlag<Map<String, Object>> anfrage = Umschlag.neu(
                execution.getBusinessKey(),
                (String) execution.getVariable("pflegehaushaltskennung"),
                HelloKonstanten.ABSENDER,
                Map.of("text", execution.getVariable("text"),
                        "kanal", execution.getVariable("kanal")));

        ausgangspostfach.ablegen(HelloKonstanten.QUEUE_ANFRAGE, anfrage);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
