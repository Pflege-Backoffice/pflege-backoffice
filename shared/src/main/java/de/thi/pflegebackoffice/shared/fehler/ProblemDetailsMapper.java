/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: @Johannes-01
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.fehler;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Wandelt ALLE Fehler aller REST-Schnittstellen in RFC-9457-Antworten um.
 * Teams muessen hier nichts tun: einfach eine Exception werfen (am besten {@link FachlicherFehler}).
 */
public class ProblemDetailsMapper {

    private static final Logger LOG = Logger.getLogger(ProblemDetailsMapper.class);
    private static final String TYPE_PREFIX = "urn:pflege-backoffice:problem:";

    @ServerExceptionMapper
    public Response fachlicherFehler(FachlicherFehler fehler, UriInfo uri) {
        return antwort(fehler.status(), fehler.code(), fehler.titel(), fehler.getMessage(), uri, null, null);
    }

    @ServerExceptionMapper
    public Response validierungsfehler(ConstraintViolationException fehler, UriInfo uri) {
        List<ProblemDetails.Verletzung> verletzungen = fehler.getConstraintViolations().stream()
                .map(v -> new ProblemDetails.Verletzung(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return antwort(400, "validierung", "Ungueltige Anfrage",
                "Die Anfrage enthaelt ungueltige Felder.", uri, verletzungen, null);
    }

    @ServerExceptionMapper
    public Response ungueltigesJson(JsonProcessingException fehler, UriInfo uri) {
        return antwort(400, "ungueltiges-json", "Ungueltiges JSON",
                fehler.getOriginalMessage(), uri, null, null);
    }

    @ServerExceptionMapper
    public Response webFehler(WebApplicationException fehler, UriInfo uri) {
        int status = fehler.getResponse().getStatus();
        String titel = Response.Status.fromStatusCode(status) != null
                ? Response.Status.fromStatusCode(status).getReasonPhrase() : "HTTP-Fehler";
        return antwort(status, "http-" + status, titel, fehler.getMessage(), uri, null, null);
    }

    @ServerExceptionMapper
    public Response unerwarteterFehler(Exception fehler, UriInfo uri) {
        String fehlerId = UUID.randomUUID().toString();
        // Technische Details nur ins Log, nicht an den Aufrufer.
        LOG.errorf(fehler, "Unerwarteter Fehler [fehlerId=%s] bei %s", fehlerId, uri.getPath());
        return antwort(500, "intern", "Interner Fehler",
                "Ein unerwarteter Fehler ist aufgetreten. Bitte fehlerId angeben.", uri, null, fehlerId);
    }

    private Response antwort(int status, String code, String titel, String detail, UriInfo uri,
                             List<ProblemDetails.Verletzung> verletzungen, String fehlerId) {
        String id = fehlerId != null ? fehlerId : UUID.randomUUID().toString();
        if (status < 500) {
            LOG.infof("Fehlerantwort %d [fehlerId=%s] bei %s: %s", status, id, uri.getPath(), detail);
        }
        ProblemDetails problem = new ProblemDetails(TYPE_PREFIX + code, titel, status, detail,
                uri.getPath(), id, Instant.now(), verletzungen);
        return Response.status(status).type(ProblemDetails.MEDIENTYP).entity(problem).build();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
