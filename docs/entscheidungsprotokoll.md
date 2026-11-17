<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: <NAME>
  Hauptautor: KI-generiert (Claude Code, Anthropic)
  KI-GENERIERT START (Claude Code, Anthropic)
-->
# Entscheidungsprotokoll

Jede teamübergreifende Entscheidung bekommt einen Eintrag. Neue Einträge **oben** anfügen,
fortlaufend nummerieren, bestehende Einträge nicht löschen (bei Änderung: neuer Eintrag, der den
alten ersetzt).

## Vorlage

```markdown
### E-XXX: <Kurztitel>

- **Datum:** JJJJ-MM-TT
- **Entscheidung:** <Was wurde entschieden?>
- **Begründung:** <Warum? Welche Alternativen wurden verworfen?>
- **Betroffene Teams:** Team 1 / Team 2 / Team 3 / alle
- **Offene Folgen:** <Was ist noch zu tun oder zu beobachten? Verweis auf offene-punkte.md>
```

---

### E-007: CIB-seven-Oberfläche als optionaler Container (Tomcat-Distribution)

- **Datum:** 2026-11-17
- **Entscheidung:** Cockpit, Tasklist und Admin laufen im Container `cibseven/cibseven:tomcat-2.2.0`
  (Compose-Profil `ui`, Port 8090, nur `127.0.0.1`) auf derselben MySQL-Datenbank wie die
  Quarkus-Anwendung. Das Startskript `docker/cibseven-ui/start.sh` schaltet den Job-Executor ab
  (`jobExecutorActivate=false` in `bpm-platform.xml`) und entfernt das Rechnungs-Beispiel.
- **Begründung:** Die Quarkus-Erweiterung von CIB seven enthält keine Webanwendungen. Die Engine
  speichert ihren gesamten Zustand in der Datenbank, deshalb kann eine zweite Engine-Instanz ihn
  anzeigen. Ohne abgeschalteten Job-Executor würde der Container Timer und asynchrone Schritte
  übernehmen, deren Java-Delegates nur in Quarkus existieren → Vorfälle. Feste Version statt
  `tomcat-latest`, weil die Engine-Version zu den Tabellen der Quarkus-Engine passen muss (am
  2026-11-17 waren beide Tags dasselbe Image). Das Rechnungs-Beispiel würde Prozesse, Instanzen und
  Demo-Benutzer in unsere Datenbank schreiben.
- **Geprüft (lokal):** Ein Timer-Prozess (1 s) läuft im unveränderten Image weiter, mit dem
  Startskript bleibt er stehen. Cockpit zeigt Modell und Instanzen des Hello-Prozesses; Quarkus
  arbeitet parallel weiter. Auf leerer Datenbank legt das Setup-Formular den ersten Admin an.
- **Betroffene Teams:** alle (nur lokale Entwicklung; CI startet den Container nicht)
- **Offene Folgen:** Image-Tag bei jedem CIB-seven-Upgrade mitziehen (siehe E-001).

### E-006: BPMN-Service-Tasks nur über Delegate-Beans

- **Datum:** 2026-11-16
- **Entscheidung:** Service- und Sendeaufgaben verwenden `camunda:delegateExpression="${beanName}"`
  mit einer `JavaDelegate`-Klasse (`@Named`, `@Dependent`). Keine Methodenaufrufe in Ausdrücken.
- **Begründung:** Unter Quarkus (ArC) unterstützt der CDI-EL-Resolver von CIB seven keine
  Methodenaufrufe (`UnsupportedOperationException`); Bean-Namen funktionieren.
- **Betroffene Teams:** alle
- **Offene Folgen:** keine

### E-005: MySQL-Port 3307 auf dem Rechner

- **Datum:** 2026-11-16
- **Entscheidung:** Der MySQL-Container ist auf dem Host unter Port 3307 erreichbar.
- **Begründung:** Viele Rechner haben bereits ein lokales MySQL auf 3306; das führte zu
  „Access denied“, weil die Anwendung das falsche MySQL erreichte.
- **Betroffene Teams:** alle
- **Offene Folgen:** in `.env` änderbar (`MYSQL_PORT`), dann auch als Umgebungsvariable beim Start setzen.

### E-004: Eine MySQL-Datenbank für Engine- und Fachtabellen

- **Datum:** 2026-11-16
- **Entscheidung:** Eine Datenbank `pflege_backoffice`; Engine-Tabellen `ACT_*`, Fachtabellen mit
  Präfix (`hello_`, Vorschlag `t1_`, `t2_`, `t3_`). Hibernate legt Fachtabellen an
  (`update`, in Tests `drop-and-create`).
- **Begründung:** Einfachster Aufbau; Prozess- und Fachdaten in einer Transaktion.
- **Betroffene Teams:** alle
- **Offene Folgen:** Für spätere Phasen Schema-Migration (z. B. Flyway) erwägen.

### E-003: Paket- und Gruppenname

- **Datum:** 2026-11-16
- **Entscheidung:** Maven-`groupId` und Java-Basispaket `de.thi.pflegebackoffice`;
  Team-Pakete `…team1`, `…team2`, `…team3`.
- **Begründung:** Eindeutig, kurz, keine Konflikte zwischen den Teams.
- **Betroffene Teams:** alle
- **Offene Folgen:** keine

### E-002: Eine Quarkus-Anwendung mit Team-Modulen (modularer Monolith)

- **Datum:** 2026-11-16
- **Entscheidung:** Eine startbare Quarkus-Anwendung (`app`); jedes Team arbeitet in einem eigenen
  Maven-Modul. Gemeinsames in `shared`.
- **Begründung:** Ein `./mvnw quarkus:dev`, eine Engine, ein Port – geringer Einrichtungsaufwand für
  12 Personen; teamübergreifende Prozesse lassen sich einfach korrelieren. Alternative „eine
  Anwendung je Team“ verworfen wegen dreifacher Infrastruktur und höherem Abstimmungsaufwand.
- **Betroffene Teams:** alle
- **Offene Folgen:** Ein fehlerhafter Build eines Teams blockiert alle → CI auf jedem Pull Request.
  Team-Grenzen (Pakete, Queues, Tabellen, REST-Pfade) einhalten, damit ein Herauslösen möglich bleibt.

### E-001: Versionen

- **Datum:** 2026-11-16
- **Entscheidung:** JDK 17, Quarkus-Plattform 3.27.6 (inkl. Camel Quarkus über `quarkus-camel-bom`),
  CIB seven 2.2.0, Esper 9.0.0, MySQL 8.4, ActiveMQ Classic 6.2.0 (Docker).
- **Begründung:** Laut Kompatibilitätstabelle von CIB seven
  (<https://docs.cibseven.org/manual/latest/user-guide/quarkus-integration/version-compatibility/>)
  gehört CIB seven 2.2.x zu Quarkus 3.27.x; 3.27.6 ist der neueste Patchstand dieser Linie.
  Neuere Quarkus-Versionen (3.40, 4.0) werden von CIB seven nicht unterstützt.
- **Betroffene Teams:** alle
- **Offene Folgen:** Upgrades nur gemeinsam und nur auf ein unterstütztes Versionspaar.
<!-- KI-GENERIERT ENDE (Claude Code, Anthropic) -->
