/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.api;

import de.thi.pflegebackoffice.hello.domain.HelloStatus;

import java.util.List;

/**
 * Status eines Hello-Vorgangs: fachlicher Status aus MySQL plus technischer Zustand
 * der Prozessinstanz (laeuft sie noch, an welchem BPMN-Element wartet sie).
 */
public record HelloStatusAntwort(
        String vorgangskennung,
        String pflegehaushaltskennung,
        HelloStatus status,
        String antwort,
        boolean prozessAktiv,
        List<String> aktiveSchritte) {
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
