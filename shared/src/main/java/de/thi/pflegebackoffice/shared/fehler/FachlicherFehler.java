/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: @Johannes-01
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.shared.fehler;

/**
 * Basisklasse fuer erwartbare, fachliche Fehler. Wird automatisch in eine
 * RFC-9457-Antwort umgewandelt (siehe {@link ProblemDetailsMapper}).
 * <p>
 * Beispiel: {@code throw FachlicherFehler.nichtGefunden("Vorgang V-1 existiert nicht");}
 */
public class FachlicherFehler extends RuntimeException {

    private final int status;
    private final String code;
    private final String titel;

    public FachlicherFehler(int status, String code, String titel, String detail) {
        super(detail);
        this.status = status;
        this.code = code;
        this.titel = titel;
    }

    public static FachlicherFehler nichtGefunden(String detail) {
        return new FachlicherFehler(404, "nicht-gefunden", "Ressource nicht gefunden", detail);
    }

    public static FachlicherFehler konflikt(String detail) {
        return new FachlicherFehler(409, "konflikt", "Konflikt mit dem aktuellen Zustand", detail);
    }

    public int status() {
        return status;
    }

    /** Kurzer, maschinenlesbarer Fehlercode; wird Teil der type-URI. */
    public String code() {
        return code;
    }

    public String titel() {
        return titel;
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
