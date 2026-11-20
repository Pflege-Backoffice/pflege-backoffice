/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.ausgangspostfach;

/**
 * CDI-Ereignis: eine Nachricht aus dem Ausgangspostfach hat das System tatsaechlich verlassen.
 * Beobachten mit {@code void beiVersand(@Observes NachrichtVersendet ereignis)}, z. B. um eine
 * Zeitueberwachung zu starten. Beobachter sollten schnell sein und keine Ausnahmen werfen.
 */
public record NachrichtVersendet(String ziel, String vorgangskennung) {
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
