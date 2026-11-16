/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.camel;

import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import de.thi.pflegebackoffice.shared.nachricht.UmschlagFormate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.cibseven.bpm.engine.MismatchingMessageCorrelationException;

/**
 * Alle Camel-Routen des Hello-Prozesses. Die Nummern beziehen sich auf die Pflichtpunkte
 * im README (Abschnitt "Hello-Prozess").
 */
@ApplicationScoped
public class HelloRouten extends RouteBuilder {

    @Inject
    HelloAusgangDienst ausgangDienst;

    @Inject
    HelloKorrelation korrelation;

    @Override
    public void configure() {

        // Fehlerfall zu (4): Antwort passt zu keinem wartenden Prozess -> Fehler-Queue statt Endlos-Wiederholung
        onException(MismatchingMessageCorrelationException.class)
                .handled(true)
                .log(LoggingLevel.WARN, "Antwort passt zu keinem wartenden Prozess: ${exception.message}")
                .marshal(UmschlagFormate.json())
                .to(HelloKonstanten.QUEUE_ANTWORT_FEHLER);

        // (2) JMS-Queue lesen und INHALTSBASIERT an zwei Ziele verteilen:
        //     kanal = DATEI     -> JSON wird in XML umgewandelt und als Datei abgelegt
        //     sonst (DATENBANK) -> als JSON in der Tabelle hello_ausgangsnachricht gespeichert
        from(HelloKonstanten.QUEUE_ANFRAGE).routeId("hello-anfrage-verteilen")
                .log("Anfrage empfangen: ${body}")
                .unmarshal(UmschlagFormate.json())                          // JSON -> Umschlag
                .process(e -> e.getMessage().setHeader("vorgangskennung",
                        e.getMessage().getBody(Umschlag.class).vorgangskennung()))
                .choice()
                    .when(e -> "DATEI".equals(e.getMessage().getBody(Umschlag.class).nutzdatenFeld("kanal")))
                        .marshal(UmschlagFormate.xml())                     // Umschlag -> XML
                        .to("file:{{hello.ausgang.verzeichnis}}?fileName=${header.vorgangskennung}.xml")
                        .log("Anfrage ${header.vorgangskennung} als XML-Datei abgelegt")
                    .otherwise()
                        .bean(ausgangDienst, "speichern")
                        .log("Anfrage ${header.vorgangskennung} in der Datenbank gespeichert")
                .end();

        // (3) Backend simulieren: Datei im Eingangsordner -> (XML wird zu JSON) -> JMS-Queue hello.antwort
        //     Alternative ohne Datei: REST POST /api/v1/hello/backend-antworten (z. B. aus Postman)
        from("file:{{hello.backend.eingang}}?include=.*\\.(xml|json)&move=.verarbeitet")
                .routeId("hello-backend-simulator-datei")
                .choice()
                    .when(header(Exchange.FILE_NAME).endsWith(".xml"))
                        .unmarshal(UmschlagFormate.xml())                   // XML -> Umschlag
                        .marshal(UmschlagFormate.json())                    // Umschlag -> JSON
                .end()
                .convertBodyTo(String.class)
                .log("Backend-Antwort aus Datei ${header.CamelFileName} gesendet")
                .to(HelloKonstanten.QUEUE_ANTWORT);

        // (4) Backend-Antwort lesen und ueber den Business Key (= Vorgangskennung) an den
        //     Prozess korrelieren, der am Nachrichten-Zwischenereignis wartet
        from(HelloKonstanten.QUEUE_ANTWORT).routeId("hello-antwort-korrelieren")
                .unmarshal(UmschlagFormate.json())
                .bean(korrelation, "korrelieren");
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
