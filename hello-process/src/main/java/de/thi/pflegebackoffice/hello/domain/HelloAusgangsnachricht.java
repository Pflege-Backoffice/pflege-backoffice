/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Ziel "Datenbank" der inhaltsbasierten Camel-Route: jede Anfrage mit kanal=DATENBANK
 * wird hier als JSON abgelegt.
 */
@Entity
@Table(name = "hello_ausgangsnachricht")
public class HelloAusgangsnachricht extends PanacheEntity {

    @Column(nullable = false, length = 64)
    public String vorgangskennung;

    @Column(nullable = false, length = 4000)
    public String inhaltJson;

    public Instant empfangenAm;

    public static long anzahlFuer(String vorgangskennung) {
        return count("vorgangskennung", vorgangskennung);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
