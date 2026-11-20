/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.ausgangspostfach;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

/**
 * Eine ausgehende Nachricht im Ausgangspostfach ("Transactional Outbox").
 * <p>
 * Wird in DERSELBEN Transaktion geschrieben wie die fachlichen Daten und der Prozesszustand.
 * Erst nach dem Commit sieht {@link AusgangspostfachVersand} den Eintrag und versendet ihn.
 */
@Entity
@Table(name = "gemeinsam_ausgangspostfach",
        indexes = @Index(name = "ix_ausgangspostfach_offen", columnList = "gesendetAm, fehlversuche, id"))
public class AusgangspostfachEintrag extends PanacheEntity {

    /** Camel-Endpunkt, z. B. {@code activemq:queue:hello.anfrage}. */
    @Column(nullable = false, length = 255)
    public String ziel;

    /** Nur fuer Nachverfolgung und Log; die Nachricht selbst steht in {@link #inhalt}. */
    @Column(length = 64)
    public String vorgangskennung;

    /** Nachrichtentext, so wie er versendet wird (in der Regel der Umschlag als JSON). */
    @Lob
    @Column(nullable = false)
    public String inhalt;

    @Column(nullable = false)
    public Instant erstelltAm;

    /** {@code null} = noch nicht versendet. */
    public Instant gesendetAm;

    @Column(nullable = false)
    public int fehlversuche;

    @Column(length = 1000)
    public String letzterFehler;

    public static List<AusgangspostfachEintrag> fuerVorgang(String vorgangskennung) {
        return list("vorgangskennung = ?1 order by id", vorgangskennung);
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
