/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello;

/** Alle Namen des Hello-Prozesses an einer Stelle (Prozess, Nachrichten, Queues). */
public final class HelloKonstanten {

    /** Prozess-ID in hello-process.bpmn. */
    public static final String PROZESS_KEY = "hello-process";

    /** Name, mit dem dieses Modul als Absender im Umschlag auftritt. */
    public static final String ABSENDER = "hello-process";

    /** BPMN-Nachricht: Antwort des (simulierten) Backends. */
    public static final String NACHRICHT_BACKEND_ANTWORT = "HelloBackendAntwort";
    /** BPMN-Nachricht: Esper hat erkannt, dass keine Antwort kam. */
    public static final String NACHRICHT_KEINE_ANTWORT = "HelloKeineAntwort";

    /** JMS-Queue: Prozess -> Backend (Sendeaufgabe). */
    public static final String QUEUE_ANFRAGE = "activemq:queue:hello.anfrage";
    /** JMS-Queue: Backend -> Prozess (Antworten zum Korrelieren). */
    public static final String QUEUE_ANTWORT = "activemq:queue:hello.antwort";
    /** JMS-Queue: Antworten, zu denen kein wartender Prozess gefunden wurde. */
    public static final String QUEUE_ANTWORT_FEHLER = "activemq:queue:hello.antwort.fehler";

    private HelloKonstanten() {
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
