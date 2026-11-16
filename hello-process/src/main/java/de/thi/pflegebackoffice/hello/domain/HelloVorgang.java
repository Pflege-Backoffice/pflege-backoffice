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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Optional;

/**
 * Ein Hello-Vorgang in MySQL (Hibernate ORM mit Panache, "Active Record"-Stil:
 * oeffentliche Felder, Abfragen als statische Methoden).
 */
@Entity
@Table(name = "hello_vorgang")
public class HelloVorgang extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 64)
    public String vorgangskennung;

    @Column(nullable = false, length = 64)
    public String pflegehaushaltskennung;

    @Column(length = 500)
    public String text;

    @Column(length = 20)
    public String kanal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    public HelloStatus status;

    @Column(length = 500)
    public String antwort;

    public Instant erstelltAm;
    public Instant geaendertAm;

    public static Optional<HelloVorgang> findeMitKennung(String vorgangskennung) {
        return find("vorgangskennung", vorgangskennung).firstResultOptional();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
