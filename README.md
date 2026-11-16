<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: <NAME>
  Hauptautor: KI-generiert (Claude Code, Anthropic)
  KI-GENERIERT START (Claude Code, Anthropic)
-->
# Pflege-Backoffice MVP

> **Lizenz: MIT** – Der Quellcode dieses Repositorys steht unter der [MIT-Lizenz](LICENSE)
> (© 2026 Pflege-Backoffice MVP contributors) und darf frei, auch kommerziell, genutzt werden.
> **Achtung:** Eine verwendete Pflicht-Bibliothek (Esper) steht unter GPL v2 – siehe
> [THIRD_PARTY_LICENSES.md](THIRD_PARTY_LICENSES.md) und [offene Punkte](docs/offene-punkte.md) (OP-001).

Gemeinsames Starter-Repository für das Projekt „Pflege-Backoffice MVP“ im Kurs
**Digital Process Engineering (THI)**. Drei Teams bauen eine gemeinsame prozessgesteuerte Anwendung:

| Team | Prozess | Zusätzliche Verantwortung | Modul |
|---|---|---|---|
| Team 1 | Verwaltung und Erstattung (Rechnungen/Belege bis zum bestätigten Ergebnis) | Pflegehaushalts- und Vorgangsakte, Dokumentzuordnung, teamübergreifende Vorgangsübersicht | `team1-verwaltung` |
| Team 2 | Materialversorgung (Nachbestellung von Verbrauchsmaterial) | Kundenfreigaben, Benachrichtigungen, konfigurierbare Berechtigungsgrenzen | `team2-material` |
| Team 3 | Geplante Abwesenheit (Ersatzpflege) | Externe Ansprechpartner, Kontakt, Fristen und Wiedervorlagen | `team3-abwesenheit` |

Das Repository enthält noch **keine Fachlogik**, sondern einen lauffähigen Beispielprozess
(„Hello-Prozess“), der alle geforderten Techniken zeigt. **Alle Daten sind synthetisch** – keine
echten Namen, Gesundheits- oder Pflegedaten verwenden!

## Inhalt

1. [Technik und Versionen](#technik-und-versionen)
2. [Voraussetzungen](#voraussetzungen)
3. [Schnellstart](#schnellstart)
4. [Hello-Prozess durchspielen](#hello-prozess-durchspielen)
5. [Tests](#tests)
6. [Modulübersicht](#modulübersicht)
7. [Entwicklungsumgebung einrichten](#entwicklungsumgebung-einrichten)
8. [Kennzeichnung der Autorenschaft](#kennzeichnung-der-autorenschaft)
9. [Zusammenarbeit: Branches und Pull Requests](#zusammenarbeit-branches-und-pull-requests)
10. [Fehlerbehebung](#fehlerbehebung)
11. [Weitere Dokumentation](#weitere-dokumentation)

## Technik und Versionen

| Baustein | Version | Hinweis |
|---|---|---|
| Java | JDK 17 | `maven.compiler.release=17` |
| Quarkus | 3.27.6 | Plattform-BOM `io.quarkus.platform:quarkus-bom` |
| CIB seven (BPMN-Engine, Camunda-7-Fork) | 2.2.0 | `org.cibseven.bpm.quarkus:cibseven-bpm-quarkus-engine`; laut [Kompatibilitätstabelle](https://docs.cibseven.org/manual/latest/user-guide/quarkus-integration/version-compatibility/) passend zu Quarkus 3.27.x |
| Apache Camel (Camel Quarkus) | 4.14 / 3.27.x | über `io.quarkus.platform:quarkus-camel-bom` 3.27.6 |
| ActiveMQ Classic | 6.2.0 | Docker-Image `apache/activemq-classic` |
| MySQL | 8.4 | Docker-Image `mysql` |
| Esper | 9.0.0 | Ereignisstromverarbeitung |
| Maven | 3.9.16 | über den Maven Wrapper `./mvnw` – keine eigene Installation nötig |

Begründung der Versionswahl: [docs/entscheidungsprotokoll.md](docs/entscheidungsprotokoll.md), E-001.

## Voraussetzungen

| | macOS | Windows 10/11 | Linux |
|---|---|---|---|
| **JDK 17** | `brew install openjdk@17` oder [Adoptium Temurin 17](https://adoptium.net/) | [Adoptium Temurin 17](https://adoptium.net/) (MSI, „Set JAVA_HOME“ anhaken) | `sudo apt install openjdk-17-jdk` (bzw. Paketmanager der Distribution) |
| **Docker** | [Docker Desktop](https://www.docker.com/products/docker-desktop/) | [Docker Desktop](https://www.docker.com/products/docker-desktop/) mit WSL 2 | Docker Engine + Compose-Plugin oder Docker Desktop |
| **Git** | `xcode-select --install` oder `brew install git` | [Git for Windows](https://git-scm.com/download/win) | `sudo apt install git` |

Prüfen (alle Systeme):

```bash
java -version          # muss 17.x anzeigen
docker compose version
git --version
```

Maven muss **nicht** installiert werden – der Maven Wrapper (`./mvnw` bzw. `mvnw.cmd`) lädt die
richtige Version automatisch. Optional: [Postman](https://www.postman.com/downloads/) für die Beispiel-Requests.

## Schnellstart

```bash
# 1. Repository holen
git clone https://github.com/Pflege-Backoffice/pflege-backoffice.git
cd pflege-backoffice

# 2. Lokale Zugangsdaten anlegen (Datei .env wird nie eingecheckt)
cp .env.example .env              # Windows: copy .env.example .env

# 3. MySQL und ActiveMQ starten
docker compose up -d
docker compose ps                 # mysql sollte "healthy" sein (dauert ca. 20 s)

# 4. Anwendung im Entwicklungsmodus starten (Live-Reload, Debug-Port 5005)
./mvnw quarkus:dev                # Windows: mvnw.cmd quarkus:dev
```

Der erste Start lädt alle Abhängigkeiten und dauert einige Minuten. Danach:

| Was | Adresse |
|---|---|
| Swagger UI (alle Endpunkte ausprobieren) | <http://localhost:8080/q/swagger-ui> |
| OpenAPI-Beschreibung | <http://localhost:8080/q/openapi> |
| Quarkus Dev UI | <http://localhost:8080/q/dev-ui> |
| Health-Check | <http://localhost:8080/q/health> |
| ActiveMQ-Konsole (Queues ansehen) | <http://localhost:8161/admin> (Zugang aus `.env`: `admin` / `admin-dev`) |
| MySQL | `localhost:3307`, Datenbank `pflege_backoffice` (Zugang aus `.env`) |

Beenden: `q` im Quarkus-Terminal bzw. `Strg+C`; Container stoppen mit `docker compose down`
(`docker compose down -v` löscht zusätzlich alle Daten).

## Hello-Prozess durchspielen

Der Hello-Prozess zeigt alle Pflichtpunkte in einem Durchlauf (Details und Diagramm:
[docs/architektur.md](docs/architektur.md)):

```
REST start ─► BPMN Sendeaufgabe ─► JMS hello.anfrage ─► Camel (inhaltsbasiert) ─► XML-Datei | DB-Tabelle
                     │
                     ▼ wartet am Ereignis-Gateway auf …
   … Backend-Antwort:  REST/Datei ─► JMS hello.antwort ─► Camel ─► Korrelation (Business Key)
   … oder Esper:       keine Antwort im Zeitfenster (120 s) ─► Nachricht "HelloKeineAntwort"
```

### Mit Postman (empfohlen, auch unter Windows)

1. In Postman **Import** → beide Dateien aus [`postman/`](postman/) importieren.
2. Oben rechts die Umgebung **„Pflege-Backoffice lokal“** wählen.
3. Requests **1 → 2 → 3 → 4** nacheinander senden. Schritt 1 erzeugt automatisch eine neue Vorgangskennung.

### Mit curl (macOS/Linux, Git Bash unter Windows)

```bash
VK=V-DEMO-$(date +%s)
TS=$(date -u +%Y-%m-%dT%H:%M:%SZ)

# 1) Prozess starten. kanal = DATEI  -> Camel legt XML-Datei ab
#                     kanal = DATENBANK -> Camel speichert in Tabelle hello_ausgangsnachricht
curl -i -X POST http://localhost:8080/api/v1/hello/vorgaenge \
  -H 'Content-Type: application/json' \
  -d "{\"vorgangskennung\":\"$VK\",\"pflegehaushaltskennung\":\"H-DEMO-0001\",\"zeitstempel\":\"$TS\",\"absender\":\"curl\",\"nutzdaten\":{\"text\":\"Hallo Backend\",\"kanal\":\"DATEI\"}}"
# -> 201, "status":"WARTET_AUF_ANTWORT", "aktiveSchritte":["AufAntwortWarten"]

# 2) Ergebnis der Camel-Route ansehen (JSON wurde in XML umgewandelt)
cat app/target/hello/ausgang/$VK.xml

# 3) Backend simulieren: Antwort auf die Queue hello.antwort legen (innerhalb von 120 s!)
curl -i -X POST http://localhost:8080/api/v1/hello/backend-antworten \
  -H 'Content-Type: application/json' \
  -d "{\"vorgangskennung\":\"$VK\",\"pflegehaushaltskennung\":\"H-DEMO-0001\",\"zeitstempel\":\"$TS\",\"absender\":\"backend-simulator\",\"nutzdaten\":{\"text\":\"Hallo Prozess\"}}"
# -> 202

# 4) Status abfragen
curl http://localhost:8080/api/v1/hello/vorgaenge/$VK
# -> "status":"ABGESCHLOSSEN", "antwort":"Hallo Prozess", "prozessAktiv":false
```

**Variante Backend als Datei (XML → JSON):** statt Schritt 3 die Datei
[`beispieldaten/backend-antwort.xml`](beispieldaten/backend-antwort.xml) kopieren, darin die
`vorgangskennung` anpassen und nach `app/target/hello/backend-eingang/` legen. Camel liest sie,
wandelt sie in JSON um und legt sie auf `hello.antwort`; verarbeitete Dateien landen in `.verarbeitet/`.

**Variante Esper (keine Antwort):** Schritt 3 weglassen. Nach 120 s meldet Esper die fehlende
Antwort an den Prozess, der Status wird `KEINE_ANTWORT`. Das Zeitfenster lässt sich mit
`hello.esper.timeout-sekunden` in `application.properties` ändern.

**Fehlerformat ansehen:** `curl -i http://localhost:8080/api/v1/hello/vorgaenge/GIBT-ES-NICHT`
→ `404` mit `application/problem+json` (RFC 9457) und `fehlerId`, die auch im Log steht.

**Datenbank ansehen:**

```bash
docker exec -it pflege-mysql mysql -upflege -ppflege-dev pflege_backoffice \
  -e "select vorgangskennung, status, antwort from hello_vorgang;"
```

## Tests

```bash
docker compose up -d     # Tests brauchen MySQL und ActiveMQ
./mvnw verify            # Windows: mvnw.cmd verify
```

- `shared`: Unit-Test der JSON ↔ XML-Umwandlung (ohne Infrastruktur).
- `app`: Ende-zu-Ende-Tests des Hello-Prozesses (`HelloProzessTest`): beide Routing-Ziele,
  Backend per REST und per XML-Datei, Esper-Zeitüberschreitung (im Test 3 s), Fehlerformat.
- GitHub Actions führt `./mvnw verify` mit JDK 17 bei jedem Push und Pull Request aus
  ([.github/workflows/build.yml](.github/workflows/build.yml)).

## Modulübersicht

```
pflege-backoffice/
├── pom.xml                 Eltern-POM: Versionen (BOMs), Java 17, Modulliste
├── shared/                 Gemeinsam: Umschlag (Nachrichtenformat), JSON/XML, RFC-9457-Fehler
├── cep-esper/              Kapselung der Esper-Engine (nur hier com.espertech-Klassen!)
├── hello-process/          Beispielprozess = Vorlage für die Teams
│   ├── api/                REST-Ressource und DTOs
│   ├── camel/              Camel-Routen, Korrelation
│   ├── cep/                Esper-Regel "keine Antwort"
│   ├── domain/             Panache-Entities (MySQL)
│   ├── prozess/            BPMN-Delegates, Deployment, Fachdienst
│   └── resources/bpmn/     hello-process.bpmn
├── team1-verwaltung/       Team 1 (noch leer)
├── team2-material/         Team 2 (noch leer)
├── team3-abwesenheit/      Team 3 (noch leer)
├── app/                    Die EINE Quarkus-Anwendung: application.properties, E2E-Tests
├── docs/                   Architektur, API-Konventionen, Entscheidungen, offene Punkte
├── postman/                Postman-Collection und Umgebung
├── beispieldaten/          Synthetische Beispieldateien
└── docker-compose.yml      MySQL + ActiveMQ (Zugangsdaten aus .env)
```

Warum eine Anwendung mit Team-Modulen? → [Entscheidung E-002](docs/entscheidungsprotokoll.md).
Jedes Team arbeitet in seinem Modul und Paket (`de.thi.pflegebackoffice.team1` …) – das hält
Merge-Konflikte klein. Neue Abhängigkeiten trägt ein Team in **seine** `pom.xml` ein;
`shared/`, `app/` und die Eltern-`pom.xml` ändert nur der Integrationskreis.

**BPMN-Modelle bearbeiten:** mit dem [Camunda Modeler](https://camunda.com/download/modeler/)
(Typ „Camunda 7“). Wichtig unter Quarkus: Service-Tasks nur mit
`delegateExpression="${beanName}"`, keine Methodenaufrufe (siehe E-006).

## Entwicklungsumgebung einrichten

**Maven ist die einzige Wahrheit.** Projektdateien von IDEs (`.idea/`, `.project`, `.classpath`,
`.settings/`, `.vscode/` außer `extensions.json`) werden nicht eingecheckt. Formatierung regelt
[`.editorconfig`](.editorconfig) (UTF-8, LF, 4 Leerzeichen Java, 2 für XML/YAML/JSON) – VS Code
braucht dafür die Erweiterung „EditorConfig“, IntelliJ und Eclipse (mit Plugin) unterstützen es.
Alles funktioniert auch ganz ohne IDE über die Kommandozeile.

### VS Code

1. Ordner `pflege-backoffice` öffnen → die vorgeschlagenen Erweiterungen installieren
   (`.vscode/extensions.json`: Java Pack, Quarkus, Camel, XML, YAML, Docker).
2. JDK 17 einstellen: Befehlspalette → „Java: Configure Java Runtime“ → JDK 17 wählen
   (bzw. Einstellung `java.jdt.ls.java.home` / `java.configuration.runtimes`).
3. Starten im Terminal: `./mvnw quarkus:dev`.

### IntelliJ IDEA

1. **File → Open** → die **Wurzel-`pom.xml`** wählen → „Open as Project“.
2. **File → Project Structure → Project → SDK:** JDK 17, Language level 17.
3. Starten: im Terminal `./mvnw quarkus:dev` – oder (Ultimate) Run-Konfiguration „Quarkus“ für das Modul `app`.

### Eclipse IDE for Jakarta EE Developers

1. **File → Import → Maven → Existing Maven Projects** → Wurzelordner wählen → alle Module übernehmen.
2. **Window → Preferences → Java → Installed JREs:** JDK 17 hinzufügen und als Standard markieren;
   unter **Compiler** Level 17.
3. Optional: **Quarkus Tools** aus dem Eclipse Marketplace.
4. Starten im Terminal: `./mvnw quarkus:dev`.

### Debugger anhängen (alle IDEs)

`./mvnw quarkus:dev` öffnet automatisch den Debug-Port **5005**. Verbinden mit:

- **VS Code:** Run and Debug → „create a launch.json“ → Java → Konfiguration
  `{ "type": "java", "name": "Quarkus anhängen", "request": "attach", "hostName": "localhost", "port": 5005 }`
  (die Datei bleibt lokal und wird nicht eingecheckt).
- **IntelliJ IDEA:** Run → Edit Configurations → **+** → **Remote JVM Debug** → Port 5005 → Debug.
- **Eclipse:** Run → Debug Configurations → **Remote Java Application** → Host `localhost`, Port 5005.

Warten, bis der Debugger verbunden ist, bevor Quarkus startet: `./mvnw quarkus:dev -Dsuspend`.

## Kennzeichnung der Autorenschaft

Die Prüfungsordnung verlangt, dass zu jeder Datei die verantwortliche Person und KI-generierter
Code erkennbar sind. Konvention:

**1. Kopf jeder Quelldatei** (Kommentarsyntax der jeweiligen Sprache):

```java
/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
```

- `Verantwortlich:` – die Person, die für die Datei geradesteht (Platzhalter `<NAME>` ersetzen).
- `Hauptautor:` – wer den Großteil geschrieben hat. Dieses Grundgerüst wurde mit **Claude Code
  (Anthropic)** erzeugt. Schreibt eine Person die Datei überwiegend neu, wird ihr Name eingetragen.

**2. Blöcke im Code** – Anfang und Ende jedes Abschnitts werden markiert:

```java
// KI-GENERIERT START (Claude Code, Anthropic)
... von der KI erzeugter Code ...
// KI-GENERIERT ENDE (Claude Code, Anthropic)

// EIGENER CODE START (Vorname Nachname)
... selbst geschriebener Code ...
// EIGENER CODE ENDE (Vorname Nachname)
```

- Ändert eine Person Code **innerhalb** eines KI-Blocks, wird die Änderung als verschachtelter
  `EIGENER CODE`-Block markiert.
- Wird eine andere KI genutzt, steht ihr Name in der Markierung, z. B.
  `// KI-GENERIERT START (GitHub Copilot)`.
- Andere Dateitypen nutzen ihre eigene Kommentarsyntax: `<!-- … -->` (XML, BPMN, Markdown),
  `# …` (Properties, YAML, `.gitignore`).
- Ausnahmen: JSON erlaubt keine Kommentare – die Angaben stehen bei den Postman-Dateien im Feld
  `description` bzw. `_kommentar`. Unverändert übernommene Fremddateien (`LICENSE`, `mvnw`,
  `mvnw.cmd`, `.mvn/wrapper/`) bekommen keinen Kopf.

## Zusammenarbeit: Branches und Pull Requests

Einfaches Modell (GitHub Flow):

- `main` ist immer lauffähig. Niemand pusht direkt auf `main`.
- Für jede Aufgabe einen Branch von `main`: `feature/team2-bestellung-anlegen`,
  `fix/team1-statusabfrage`, `docs/api-konventionen`.
- Kleine, häufige Pull Requests gegen `main`. Der Build (GitHub Actions) muss grün sein, mindestens
  eine Person prüft. Bei Änderungen an `shared/`, `app/`, `pom.xml` oder `docs/` prüft der Integrationskreis.
- Vor dem Pull Request `main` in den Branch holen (`git pull origin main`) und `./mvnw verify` ausführen.
- Pull-Request-Vorlage: [.github/pull_request_template.md](.github/pull_request_template.md);
  Prüfer je Ordner: [.github/CODEOWNERS](.github/CODEOWNERS) (Namen noch eintragen).

## Fehlerbehebung

| Problem | Lösung |
|---|---|
| `Access denied for user 'pflege'@'localhost'` | Ein lokal installiertes MySQL belegt den Port. Das Projekt nutzt deshalb **3307**. Prüfen: `docker compose ps` zeigt `0.0.0.0:3307->3306`. Wurde `.env` vor dieser Änderung angelegt: neu aus `.env.example` kopieren. |
| Port 3307, 61616, 8161 oder 8080 belegt | In `.env` andere Ports setzen (`MYSQL_PORT`, `ACTIVEMQ_PORT`, …) und dieselben Werte beim Start als Umgebungsvariablen übergeben, z. B. `MYSQL_PORT=3308 ./mvnw quarkus:dev`; Quarkus-Port: `./mvnw quarkus:dev -Dquarkus.http.port=8081`. |
| `Cannot connect to the Docker daemon` | Docker Desktop starten. |
| `./mvnw: Permission denied` (macOS/Linux) | `chmod +x mvnw` |
| `bad interpreter: /bin/sh^M` | Zeilenenden wurden zu CRLF – mit `git config core.autocrlf false` neu auschecken; `.gitattributes` sorgt danach für LF. |
| Status bleibt `WARTET_AUF_ANTWORT` | Stimmt die `vorgangskennung` der Antwort? Antworten ohne wartenden Prozess landen in der Queue `hello.antwort.fehler` (ActiveMQ-Konsole). |
| Prozessänderung wird nicht übernommen | Nach Änderungen an `.bpmn`-Dateien Quarkus neu starten (`s` im Dev-Modus-Terminal). |

## Weitere Dokumentation

- [docs/architektur.md](docs/architektur.md) – Aufbau, Diagramm (Mermaid), Zuordnung der Pflichtpunkte
- [docs/api-konventionen.md](docs/api-konventionen.md) – **Vorschlag** für REST, Umschlag, Fehlerformat, JMS
- [docs/entscheidungsprotokoll.md](docs/entscheidungsprotokoll.md) – getroffene Entscheidungen + Vorlage
- [docs/offene-punkte.md](docs/offene-punkte.md) – offene Entscheidungen (u. a. Esper-Lizenz)
- [THIRD_PARTY_LICENSES.md](THIRD_PARTY_LICENSES.md) – Lizenzen aller Abhängigkeiten
<!-- KI-GENERIERT ENDE (Claude Code, Anthropic) -->
