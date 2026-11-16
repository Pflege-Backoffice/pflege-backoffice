/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/** Nutzdaten der (simulierten) Backend-Antwort. */
public record BackendAntwortDaten(
        @Schema(example = "Hallo Prozess, Anfrage erhalten") @NotBlank @Size(max = 500) String text) {
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
