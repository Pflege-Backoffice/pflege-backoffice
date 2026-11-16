/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.nachricht;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;

/**
 * Einheitliche JSON- und XML-Darstellung des {@link Umschlag}s.
 * <p>
 * Die Camel-Datenformate werden in Routen mit {@code .marshal(...)} (Objekt -> Text) und
 * {@code .unmarshal(...)} (Text -> Objekt) verwendet. Damit ist die Umwandlung
 * JSON -> XML und XML -> JSON jeweils nur zwei Zeilen in einer Route.
 */
public final class UmschlagFormate {

    /** JSON-Mapper: Zeitstempel als ISO-8601-Text, nicht als Zahl. */
    public static final ObjectMapper JSON_MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    /** XML-Mapper mit denselben Regeln wie der JSON-Mapper. */
    public static final XmlMapper XML_MAPPER = XmlMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    private UmschlagFormate() {
    }

    /** Camel-Datenformat: Umschlag <-> JSON. Nutzdaten werden beim Einlesen zu einer Map. */
    public static JacksonDataFormat json() {
        return new JacksonDataFormat(JSON_MAPPER, Umschlag.class);
    }

    /** Camel-Datenformat: Umschlag <-> XML. Nutzdaten werden beim Einlesen zu einer Map. */
    public static JacksonXMLDataFormat xml() {
        return new JacksonXMLDataFormat(XML_MAPPER, Umschlag.class);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
