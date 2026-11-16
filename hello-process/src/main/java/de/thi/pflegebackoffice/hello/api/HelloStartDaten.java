/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/** Nutzdaten zum Start eines Hello-Vorgangs. */
public record HelloStartDaten(
        @Schema(example = "Hallo Backend") @NotBlank @Size(max = 500) String text,
        @Schema(description = "Ziel der Camel-Route: DATEI (XML-Datei) oder DATENBANK (Tabelle)",
                example = "DATEI", enumeration = {"DATEI", "DATENBANK"})
        @NotNull @Pattern(regexp = "DATEI|DATENBANK") String kanal) {
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
