/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.ausgangspostfach;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import de.thi.pflegebackoffice.shared.nachricht.UmschlagFormate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.Instant;

/**
 * Nachrichten verlaesslich versenden: statt direkt auf eine Queue zu schreiben, legt man die
 * Nachricht hier ab. Sie wird nur dann versendet, wenn die aktuelle Transaktion erfolgreich
 * committet – also erst, wenn Fachdaten und Prozesszustand gespeichert sind.
 * <p>
 * Benutzung, z. B. in einem {@code JavaDelegate} einer BPMN-Sendeaufgabe:
 * <pre>{@code
 * @Inject Ausgangspostfach ausgangspostfach;
 * ...
 * ausgangspostfach.ablegen("activemq:queue:hello.anfrage", umschlag);
 * }</pre>
 * Warum: Wuerde direkt gesendet, koennte (a) eine schnelle Antwort ankommen, bevor der Prozess
 * am Empfangsereignis gespeichert ist (Korrelation schlaegt fehl), und (b) bei einem Rollback eine
 * Nachricht unterwegs sein, zu der es keinen Vorgang gibt.
 * <p>
 * Zustellung: "mindestens einmal". In seltenen Faellen (Absturz zwischen Senden und Commit) kommt
 * eine Nachricht doppelt an; Empfaenger sollten Duplikate ueber die Vorgangskennung erkennen.
 */
@ApplicationScoped
public class Ausgangspostfach {

    /**
     * Legt eine Nachricht ab. MANDATORY: ohne laufende Transaktion wird der Aufruf abgelehnt,
     * denn dann gaebe es nichts, an dessen Commit der Versand gekoppelt werden koennte.
     */
    @Transactional(Transactional.TxType.MANDATORY)
    public void ablegen(String ziel, String vorgangskennung, String inhalt) {
        AusgangspostfachEintrag eintrag = new AusgangspostfachEintrag();
        eintrag.ziel = ziel;
        eintrag.vorgangskennung = vorgangskennung;
        eintrag.inhalt = inhalt;
        eintrag.erstelltAm = Instant.now();
        eintrag.persist();
    }

    /** Legt einen Umschlag als JSON ab. */
    @Transactional(Transactional.TxType.MANDATORY)
    public void ablegen(String ziel, Umschlag<?> nachricht) {
        try {
            ablegen(ziel, nachricht.vorgangskennung(), UmschlagFormate.JSON_MAPPER.writeValueAsString(nachricht));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Umschlag laesst sich nicht als JSON schreiben", e);
        }
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
