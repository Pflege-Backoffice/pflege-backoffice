/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.nachricht;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

/**
 * Gemeinsamer Nachrichten-Umschlag fuer ALLE Anfragen und Nachrichten zwischen Teams
 * (REST-Bodies und JMS-Nachrichten). VORSCHLAG fuer den Integrationskreis, siehe
 * docs/api-konventionen.md.
 *
 * @param vorgangskennung        fachliche Kennung des Vorgangs (dient auch als Business Key im Prozess)
 * @param pflegehaushaltskennung Kennung des Pflegehaushalts
 * @param zeitstempel            Erzeugungszeitpunkt der Nachricht (UTC, ISO-8601)
 * @param absender               sendendes System/Team, z. B. "team1-verwaltung"
 * @param nutzdaten              die eigentlichen fachlichen Daten
 * @param <T>                    Typ der Nutzdaten
 */
@JacksonXmlRootElement(localName = "umschlag")
public record Umschlag<T>(
        @NotBlank String vorgangskennung,
        @NotBlank String pflegehaushaltskennung,
        @NotNull Instant zeitstempel,
        @NotBlank String absender,
        @NotNull @Valid T nutzdaten) {

    /** Erzeugt einen neuen Umschlag mit aktuellem Zeitstempel. */
    public static <T> Umschlag<T> neu(String vorgangskennung, String pflegehaushaltskennung,
                                      String absender, T nutzdaten) {
        return new Umschlag<>(vorgangskennung, pflegehaushaltskennung, Instant.now(), absender, nutzdaten);
    }

    /**
     * Liest ein Feld aus den Nutzdaten, wenn diese als Map vorliegen (das ist der Fall,
     * wenn eine Nachricht ohne konkreten Nutzdaten-Typ eingelesen wurde, z. B. in Camel-Routen).
     */
    public Object nutzdatenFeld(String name) {
        return nutzdaten instanceof Map<?, ?> map ? map.get(name) : null;
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
