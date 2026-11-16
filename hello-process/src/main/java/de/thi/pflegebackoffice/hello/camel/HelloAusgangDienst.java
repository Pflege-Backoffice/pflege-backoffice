/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.camel;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.thi.pflegebackoffice.hello.domain.HelloAusgangsnachricht;
import de.thi.pflegebackoffice.shared.nachricht.Umschlag;
import de.thi.pflegebackoffice.shared.nachricht.UmschlagFormate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.Instant;

/** Ziel "Datenbank" der inhaltsbasierten Route (Punkt 2). */
@ApplicationScoped
public class HelloAusgangDienst {

    @Transactional
    public void speichern(Umschlag<?> nachricht) throws JsonProcessingException {
        HelloAusgangsnachricht eintrag = new HelloAusgangsnachricht();
        eintrag.vorgangskennung = nachricht.vorgangskennung();
        eintrag.inhaltJson = UmschlagFormate.JSON_MAPPER.writeValueAsString(nachricht);
        eintrag.empfangenAm = Instant.now();
        eintrag.persist();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
