<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: <NAME>
  Hauptautor: KI-generiert (Claude Code, Anthropic)
  KI-GENERIERT START (Claude Code, Anthropic)
-->
# Offene Punkte

Offene Entscheidungen für den teamübergreifenden Integrationskreis. Erledigte Punkte mit Verweis
auf den Eintrag im `entscheidungsprotokoll.md` abhaken, nicht löschen.

## OP-001: Lizenz von Esper (GPL v2) vs. Veröffentlichung unter MIT – **mit Prof. klären**

- **Stand (geprüft 2026-11-16):**
  - EsperTech: „Esper and NEsper are open-source software available under the GNU General Public
    License (GPLv2).“ (<https://www.espertech.com/esper/esper-license/>)
  - POM `com.espertech:esper-common:9.0.0`: Lizenz „GNU General Public License Version 2“,
    **keine** Classpath-Exception. Gleiches gilt für `esper-compiler` und `esper-runtime`.
  - Kommerzielle Lizenzen bietet EsperTech auf Anfrage an.
- **Problem:** Unser eigener Code steht unter MIT. Wird die Anwendung **zusammen mit Esper
  weitergegeben** (z. B. als lauffähiges Paket), gilt für das Gesamtwerk die GPL v2. Eine
  Veröffentlichung „alles unter MIT, auch für kommerzielle Nutzung“ ist damit **nicht eindeutig
  gegeben**. Der Quellcode im Repository enthält Esper nicht (nur die Maven-Koordinaten); unsere
  Dateien selbst bleiben MIT-lizenziert.
- **Esper ist durch den Kurs vorgegeben** und wurde deshalb nicht ersetzt.
- **Bisherige Maßnahme:** Esper ist im Modul `cep-esper` gekapselt; nur `EsperDienst` verwendet
  Esper-Klassen. Ein späterer Austausch betrifft damit nur dieses Modul.
- **Zu entscheiden (Integrationskreis + Prof.):**
  1. Akzeptieren und im README dokumentieren: Code MIT, ausgelieferte Gesamtanwendung GPL v2.
  2. Kommerzielle Esper-Lizenz (unrealistisch im Kursrahmen).
  3. Esper nur als Lehr-Demo nutzen und vor einer Weitergabe ersetzen (falls der Kurs das erlaubt).
- **Betroffene Teams:** alle

## OP-002: MySQL Connector/J (GPL v2 mit Universal FOSS Exception)

- **Stand (geprüft 2026-11-16):** Lizenz „The GNU General Public License, v2 with Universal FOSS
  Exception, v1.0“ (Version 26.10.0 auf Maven Central; Quarkus 3.27 verwendet 8.3.0, gleiche Lizenz).
- **Bewertung:** Die Universal FOSS Exception erlaubt ausdrücklich, den Treiber mit Software unter
  einer OSI-anerkannten Lizenz (MIT gehört dazu) zu verbinden und die Kombination mit getrennten
  Lizenzen je Bestandteil weiterzugeben. Für das **Open-Source-Projekt unter MIT: unproblematisch.**
  Die Ausnahme gilt **nicht** für proprietäre (geschlossene) Weiterentwicklungen; ein Dritter, der
  das Projekt kommerziell und closed-source nutzt, bräuchte dann einen anderen Treiber oder eine
  Lizenz von Oracle.
- **Mögliche Alternative:** MariaDB Connector/J (LGPL 2.1) funktioniert ebenfalls mit MySQL.
- **Zu entscheiden:** Hinweis im README genügt (Vorschlag) oder Treiberwechsel.

## OP-003: Gemeinsames fachliches Datenmodell

- Aufbau der `nutzdaten` je Schnittstelle, Format der Kennungen (`V-…`, `H-…`), erlaubte Absender.
- Vorschlag in `api-konventionen.md`; Team 1 verantwortet die Pflegehaushalts- und Vorgangsakte.

## OP-004: CODEOWNERS und Branch-Schutz aktivieren

- GitHub-Konten bzw. Teams in `.github/CODEOWNERS` eintragen (derzeit auskommentiert).
- In GitHub: Branch-Schutz für `main` (Pull Request + grüner Build + 1 Review).

## OP-005: Technische Einschränkungen des Hello-Beispiels

- **Wettlauf beim Senden:** Die Sendeaufgabe legt die JMS-Nachricht noch innerhalb der
  Start-Transaktion auf die Queue. Antwortet ein Backend extrem schnell (bevor der Prozess am
  Ereignis-Gateway gespeichert ist), schlägt die Korrelation fehl und die Antwort landet in
  `hello.antwort.fehler`. Für echte Prozesse: Sendeaufgabe mit `camunda:asyncAfter` o. Ä. absichern
  oder Nachricht erst nach dem Commit senden.
- **Esper-Zustand nur im Speicher:** Nach einem Neustart vergisst Esper laufende Zeitfenster; diese
  Prozesse warten dann ohne Zeitüberwachung. Für echte Fristen eher BPMN-Timer verwenden und Esper
  für Muster über mehrere Ereignisse.
- **ActiveMQ-Client:** Camel Quarkus 3.27 bringt den Client `activemq-client-jakarta` 5.19.x mit;
  der Broker im Container ist 6.2.0. Beide sprechen OpenWire und funktionieren zusammen (getestet).
<!-- KI-GENERIERT ENDE (Claude Code, Anthropic) -->
