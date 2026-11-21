/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.app;

import de.thi.pflegebackoffice.hello.domain.HelloAusgangsnachricht;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ende-zu-Ende-Test des Hello-Prozesses gegen die echte Infrastruktur
 * (MySQL + ActiveMQ aus docker-compose.yml bzw. GitHub-Actions-Services).
 * Nur synthetische Testdaten.
 */
@QuarkusTest
class HelloProzessTest {

    private static final Duration MAX_WARTEZEIT = Duration.ofSeconds(20);

    @Test
    void kanalDatenbank_antwortPerRest_prozessWirdAbgeschlossen() {
        String vk = neueVorgangskennung();

        starten(vk, "DATENBANK")
                .body("status", equalTo("WARTET_AUF_ANTWORT"))
                .body("aktiveSchritte", hasItem("AufAntwortWarten"));

        // (2) Inhaltsbasiertes Routing: Ziel Datenbank
        await().atMost(MAX_WARTEZEIT).until(() ->
                QuarkusTransaction.requiringNew().call(() -> HelloAusgangsnachricht.anzahlFuer(vk)) == 1);

        // (3) Backend simulieren per REST -> (4) Korrelation
        given().contentType(ContentType.JSON)
                .body(umschlag(vk, "{\"text\": \"Antwort per REST\"}"))
                .post("/api/v1/hello/backend-antworten")
                .then().statusCode(202);

        await().atMost(MAX_WARTEZEIT).untilAsserted(() -> given().get("/api/v1/hello/vorgaenge/" + vk)
                .then().statusCode(200)
                .body("status", equalTo("ABGESCHLOSSEN"))
                .body("antwort", equalTo("Antwort per REST"))
                .body("prozessAktiv", equalTo(false)));

        // Zweite Antwort auf einen abgeschlossenen Vorgang: sofort 409 statt stiller Fehler-Queue
        given().contentType(ContentType.JSON)
                .body(umschlag(vk, "{\"text\": \"zu spaet\"}"))
                .post("/api/v1/hello/backend-antworten")
                .then().statusCode(409)
                .contentType("application/problem+json")
                .body("type", equalTo("urn:pflege-backoffice:problem:konflikt"));
    }

    @Test
    void backendAntwortFuerUnbekanntenVorgang_liefert404() {
        given().contentType(ContentType.JSON)
                .body(umschlag("GIBT-ES-NICHT", "{\"text\": \"an niemanden\"}"))
                .post("/api/v1/hello/backend-antworten")
                .then().statusCode(404)
                .contentType("application/problem+json")
                .body("type", equalTo("urn:pflege-backoffice:problem:nicht-gefunden"));
    }

    @Test
    void kanalDatei_antwortAlsXmlDatei_prozessWirdAbgeschlossen() throws Exception {
        String vk = neueVorgangskennung();
        starten(vk, "DATEI");

        // (2) Inhaltsbasiertes Routing: Ziel Datei, JSON wurde in XML umgewandelt
        Path ausgang = Path.of("target/hello/ausgang", vk + ".xml");
        await().atMost(MAX_WARTEZEIT).until(() -> Files.exists(ausgang));
        String xml = Files.readString(ausgang);
        assertTrue(xml.contains("<vorgangskennung>" + vk + "</vorgangskennung>"), xml);

        // (3) Backend simulieren per XML-Datei -> Camel wandelt XML in JSON -> JMS -> (4) Korrelation
        Path eingang = Path.of("target/hello/backend-eingang");
        Files.createDirectories(eingang);
        Files.writeString(eingang.resolve(vk + ".tmp"), """
                <umschlag>
                  <vorgangskennung>%s</vorgangskennung>
                  <pflegehaushaltskennung>H-TEST-0001</pflegehaushaltskennung>
                  <zeitstempel>%s</zeitstempel>
                  <absender>backend-simulator</absender>
                  <nutzdaten><text>Antwort per XML-Datei</text></nutzdaten>
                </umschlag>
                """.formatted(vk, Instant.now()));
        // erst vollstaendig schreiben, dann umbenennen, damit Camel keine halbe Datei liest
        Files.move(eingang.resolve(vk + ".tmp"), eingang.resolve(vk + ".xml"));

        await().atMost(MAX_WARTEZEIT).untilAsserted(() -> given().get("/api/v1/hello/vorgaenge/" + vk)
                .then().body("status", equalTo("ABGESCHLOSSEN"))
                .body("antwort", equalTo("Antwort per XML-Datei")));
    }

    @Test
    void keineAntwort_esperMeldetZeitueberschreitung() {
        String vk = neueVorgangskennung();
        starten(vk, "DATENBANK");

        // (7) Esper-Zeitfenster ist im Test 3 Sekunden (application.properties, Profil %test)
        await().atMost(MAX_WARTEZEIT).untilAsserted(() -> given().get("/api/v1/hello/vorgaenge/" + vk)
                .then().body("status", equalTo("KEINE_ANTWORT"))
                .body("prozessAktiv", equalTo(false)));
    }

    @Test
    void unbekannterVorgang_liefertProblemDetails() {
        given().get("/api/v1/hello/vorgaenge/GIBT-ES-NICHT")
                .then().statusCode(404)
                .contentType("application/problem+json")
                .body("status", equalTo(404))
                .body("type", equalTo("urn:pflege-backoffice:problem:nicht-gefunden"))
                .body("instance", equalTo("/api/v1/hello/vorgaenge/GIBT-ES-NICHT"))
                .body("fehlerId", notNullValue());
    }

    @Test
    void ungueltigeAnfrage_liefertProblemDetailsMitFeldern() {
        given().contentType(ContentType.JSON)
                .body(umschlag("", "{\"text\": \"x\", \"kanal\": \"FAX\"}"))
                .post("/api/v1/hello/vorgaenge")
                .then().statusCode(400)
                .contentType("application/problem+json")
                .body("type", equalTo("urn:pflege-backoffice:problem:validierung"))
                .body("verletzungen.feld", hasItem(containsString("kanal")))
                .body("verletzungen.feld", hasItem(containsString("vorgangskennung")));
    }

    @Test
    void doppelteVorgangskennung_liefertKonflikt() {
        String vk = neueVorgangskennung();
        starten(vk, "DATENBANK");
        given().contentType(ContentType.JSON)
                .body(umschlag(vk, "{\"text\": \"nochmal\", \"kanal\": \"DATENBANK\"}"))
                .post("/api/v1/hello/vorgaenge")
                .then().statusCode(409)
                .contentType("application/problem+json");
    }

    private static io.restassured.response.ValidatableResponse starten(String vk, String kanal) {
        return given().contentType(ContentType.JSON)
                .body(umschlag(vk, "{\"text\": \"Hallo Backend\", \"kanal\": \"" + kanal + "\"}"))
                .post("/api/v1/hello/vorgaenge")
                .then().statusCode(201)
                .header("Location", containsString("/api/v1/hello/vorgaenge/" + vk))
                .body("vorgangskennung", equalTo(vk));
    }

    private static String umschlag(String vk, String nutzdatenJson) {
        return """
                {
                  "vorgangskennung": "%s",
                  "pflegehaushaltskennung": "H-TEST-0001",
                  "zeitstempel": "%s",
                  "absender": "test",
                  "nutzdaten": %s
                }
                """.formatted(vk, Instant.now(), nutzdatenJson);
    }

    private static String neueVorgangskennung() {
        return "V-TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
