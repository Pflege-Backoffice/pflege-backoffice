/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.nachricht;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Prueft die Umwandlung JSON -> Objekt -> XML -> Objekt -> JSON (synthetische Testdaten). */
class UmschlagFormateTest {

    @Test
    void jsonUndXmlSindUmkehrbar() throws Exception {
        Umschlag<Map<String, Object>> original = Umschlag.neu("V-TEST-1", "H-TEST-1", "test",
                Map.of("text", "Hallo", "kanal", "DATEI"));

        String json = UmschlagFormate.JSON_MAPPER.writeValueAsString(original);
        Umschlag<?> ausJson = UmschlagFormate.JSON_MAPPER.readValue(json, Umschlag.class);

        String xml = UmschlagFormate.XML_MAPPER.writeValueAsString(ausJson);
        assertTrue(xml.startsWith("<umschlag>"), xml);
        assertTrue(xml.contains("<kanal>DATEI</kanal>"), xml);

        Umschlag<?> ausXml = UmschlagFormate.XML_MAPPER.readValue(xml, Umschlag.class);
        assertEquals(original.vorgangskennung(), ausXml.vorgangskennung());
        assertEquals(original.zeitstempel(), ausXml.zeitstempel());
        assertEquals("DATEI", ausXml.nutzdatenFeld("kanal"));
        assertEquals(json, UmschlagFormate.JSON_MAPPER.writeValueAsString(ausXml));
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
