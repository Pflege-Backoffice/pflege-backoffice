/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.thi.pflegebackoffice.hello.HelloKonstanten;
import de.thi.pflegebackoffice.hello.prozess.HelloVorgangDienst;
import de.thi.pflegebackoffice.shared.fehler.ProblemDetails;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import de.thi.pflegebackoffice.shared.nachricht.UmschlagFormate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.camel.ProducerTemplate;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

/** REST-Schnittstelle des Hello-Prozesses (Punkt 5 und Backend-Simulation aus Punkt 3). */
@Path("/api/v1/hello")
@Tag(name = "Hello-Prozess", description = "Minimaler Beispielprozess (keine Fachlogik)")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloRessource {

    @Inject
    HelloVorgangDienst dienst;

    @Inject
    ProducerTemplate camel;

    @POST
    @Path("/vorgaenge")
    @Operation(summary = "Hello-Prozess starten",
            description = "Legt den Vorgang in MySQL an und startet die Prozessinstanz (Business Key = Vorgangskennung).")
    @APIResponse(responseCode = "201", description = "Prozess gestartet")
    @APIResponse(responseCode = "400", description = "Ungueltige Anfrage",
            content = @Content(mediaType = ProblemDetails.MEDIENTYP, schema = @Schema(implementation = ProblemDetails.class)))
    @APIResponse(responseCode = "409", description = "Vorgangskennung existiert bereits",
            content = @Content(mediaType = ProblemDetails.MEDIENTYP, schema = @Schema(implementation = ProblemDetails.class)))
    public Response starten(@Valid @NotNull Umschlag<HelloStartDaten> anfrage) {
        HelloStatusAntwort status = dienst.starten(anfrage);
        return Response.created(URI.create("/api/v1/hello/vorgaenge/" + status.vorgangskennung()))
                .entity(status)
                .build();
    }

    @GET
    @Path("/vorgaenge/{vorgangskennung}")
    @Operation(summary = "Status eines Hello-Vorgangs abfragen")
    @APIResponse(responseCode = "200", description = "Aktueller Status")
    @APIResponse(responseCode = "404", description = "Vorgang unbekannt",
            content = @Content(mediaType = ProblemDetails.MEDIENTYP, schema = @Schema(implementation = ProblemDetails.class)))
    public HelloStatusAntwort status(@PathParam("vorgangskennung") String vorgangskennung) {
        return dienst.status(vorgangskennung);
    }

    @POST
    @Path("/backend-antworten")
    @Operation(summary = "Backend simulieren: Antwort auf die JMS-Queue hello.antwort legen",
            description = "Ersatz fuer ein echtes Backend-System, z. B. aus Postman. "
                    + "Die Camel-Route korreliert die Antwort anschliessend mit dem wartenden Prozess. "
                    + "Vorher wird geprueft, ob der Vorgang existiert und noch auf eine Antwort wartet.")
    @APIResponse(responseCode = "202", description = "Nachricht auf die Queue gelegt")
    @APIResponse(responseCode = "404", description = "Vorgang unbekannt",
            content = @Content(mediaType = ProblemDetails.MEDIENTYP, schema = @Schema(implementation = ProblemDetails.class)))
    @APIResponse(responseCode = "409", description = "Vorgang wartet auf keine Antwort (mehr)",
            content = @Content(mediaType = ProblemDetails.MEDIENTYP, schema = @Schema(implementation = ProblemDetails.class)))
    public Response backendAntwortSimulieren(@Valid @NotNull Umschlag<BackendAntwortDaten> antwort)
            throws JsonProcessingException {
        dienst.pruefeWartetAufAntwort(antwort.vorgangskennung());
        camel.sendBody(HelloKonstanten.QUEUE_ANTWORT, UmschlagFormate.JSON_MAPPER.writeValueAsString(antwort));
        return Response.accepted().build();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
