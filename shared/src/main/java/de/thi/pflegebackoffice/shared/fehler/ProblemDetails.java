/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: @Johannes-01
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.fehler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Einheitliche Fehlerantwort nach RFC 9457 ("Problem Details for HTTP APIs"),
 * Medientyp {@code application/problem+json}.
 * <p>
 * Die Felder type, title, status, detail und instance sind im RFC definiert.
 * fehlerId, zeitstempel und verletzungen sind Erweiterungen dieses Projekts:
 * Die fehlerId steht auch im Server-Log, damit jeder Fehler nachvollziehbar ist.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String fehlerId,
        Instant zeitstempel,
        List<Verletzung> verletzungen) {

    public static final String MEDIENTYP = "application/problem+json";

    /** Ein einzelner Validierungsfehler (Feld + Meldung). */
    public record Verletzung(String feld, String meldung) {
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
