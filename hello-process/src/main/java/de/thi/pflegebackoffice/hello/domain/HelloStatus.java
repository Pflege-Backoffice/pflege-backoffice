/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.domain;

/** Fachlicher Status eines Hello-Vorgangs. */
public enum HelloStatus {
    /** Prozess gestartet, Anfrage an das Backend ist unterwegs. */
    WARTET_AUF_ANTWORT,
    /** Backend hat geantwortet. */
    ABGESCHLOSSEN,
    /** Esper hat erkannt: keine Antwort innerhalb des Zeitfensters. */
    KEINE_ANTWORT
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
