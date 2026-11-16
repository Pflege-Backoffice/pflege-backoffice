<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: @Johannes-01
  Hauptautor: @Johannes-01
-->

# Task: Set up the shared starter repository for our university project

## Context
We are students in the course "Digital Process Engineering" (THI). Three teams of four
students build one shared process-driven application called "Pflege-Backoffice MVP":
- Team 1: Verwaltung und Erstattung (invoices/receipts until confirmed result) – also owns
  the shared household/case file ("Pflegehaushalts- und Vorgangsakte"), document assignment
  and the cross-team case overview
- Team 2: Materialversorgung (consumables reordering) – also owns customer approvals,
  notifications, configurable permission limits
- Team 3: Geplante Abwesenheit (substitute care) – also owns external counterparts,
  contact, deadlines and follow-ups

The project handbook requires ONE shared source repository, published under the MIT license.

## Mandatory tech stack (set by the course – do not substitute)
- JDK 17 (pin with maven.compiler.release=17)
- Quarkus (microservices framework), Maven with the Maven wrapper (./mvnw)
- CIB seven BPMN engine (Camunda 7 fork), embedded via the CIB seven Quarkus extension
  (org.cibseven.bpm.quarkus:cibseven-bpm-quarkus-engine)
- Apache ActiveMQ Classic (JMS)
- Apache Camel via Camel Quarkus
- Esper (event stream processing)
- MySQL (recommended database)
- REST services

Version rule: CIB seven's Quarkus extension is bound to specific Quarkus versions. Check the
current compatibility table at
https://docs.cibseven.org/manual/latest/user-guide/quarkus-integration/version-compatibility/
and choose a supported CIB seven / Quarkus pair. Align Camel Quarkus through the matching
Quarkus platform BOM. Tell me which versions you chose and why before using them.

## Mandatory technical points the skeleton must already demonstrate
Build ONE minimal example process ("hello process", no real business logic) that shows:
1. A BPMN send task that puts an asynchronous message on a JMS queue.
2. A Camel route that reads this message from the queue and routes it to at least two
   targets (filesystem folder and/or DB table) using content-based routing, with
   bidirectional transformation between JSON and XML.
3. A way to simulate a backend system: a Camel route that reads a file from a folder (or a
   REST endpoint usable from Postman) and puts the message on a JMS queue.
4. A Camel route that takes that backend message and correlates it to a process instance
   waiting at a BPMN message intermediate catch event (correlation via a business key).
5. A REST endpoint to start the process and query its status.
6. Persistence of at least one entity in MySQL (Hibernate ORM with Panache).
7. A minimal Esper example (e.g. detect that no response arrived within a time window) and
   show how its result could feed back into the process.
Keep each piece as small and readable as possible – all 12 students must be able to explain it.

## Repository requirements
- Maven multi-module structure. Propose a layout that lets the three teams work in parallel
  with few merge conflicts, e.g. a parent POM, a shared module for common business objects
  and message formats, and separate modules or Quarkus apps per team process. Explain the
  trade-off between one Quarkus app vs. several, then let me decide.
- docker-compose.yml for ActiveMQ Classic (official apache/activemq-classic image) and MySQL,
  with credentials read from a .env file. Commit only .env.example, never real secrets.
- .gitignore for Java/Maven/Quarkus/VS Code/macOS.
- .vscode/extensions.json recommending: vscjava.vscode-java-pack, redhat.vscode-quarkus,
  redhat.apache-camel-extension-pack, redhat.vscode-xml, redhat.vscode-yaml,
  ms-azuretools.vscode-docker.
- LICENSE with the full official MIT text (copyright holder placeholder: "Pflege-Backoffice
  MVP contributors", year 2026).
- README.md in German: project purpose, MIT notice clearly visible, prerequisites,
  step-by-step local start (docker compose up, ./mvnw quarkus:dev), how to run the hello
  process end to end with example requests, module overview.
- THIRD_PARTY_LICENSES.md listing every dependency with its license. Check every license for
  compatibility with publishing the whole result under MIT, including commercial use.
  In particular, verify the licenses of Esper and the MySQL JDBC driver. If any dependency
  is not clearly compatible, do NOT silently replace it (Esper is mandatory) – document the
  issue in docs/offene-punkte.md as an open decision for the cross-team integration circle.
- docs/ folder with: entscheidungsprotokoll.md (template: date, decision, rationale, affected
  teams, open consequences), offene-punkte.md, architektur.md (short description + diagram
  in Mermaid of process, JMS queues, Camel routes, DB).
- Synthetic test data only. No real names, health or care data anywhere.
- A GitHub Actions workflow that builds and runs tests with JDK 17 (./mvnw verify).
- CODEOWNERS and a short pull request template; suggest a simple branching model (main +
  feature branches via pull requests).

## Code authorship rules (required by the examiner – very important)
- Every source file needs a header comment naming the responsible person. Use the
  placeholder "Verantwortlich: <NAME>" – we fill in names later.
- AI-generated code must be clearly marked with start and end comments and must name the
  AI used (Claude Code). Since you generate most of this skeleton, mark you as the main
  author in each file header, e.g. "Hauptautor: KI-generiert (Claude Code, Anthropic)".
  Later, students mark the beginning and end of their own code inside these files.
- Add a section to the README explaining this marking convention.

## How to work
1. First read this task, then reply with: chosen versions (with source), proposed module
   layout, and a list of open questions (max. 5). Do not create files yet.
2. After I confirm, build the skeleton step by step. After each major step (build compiles,
   containers start, process runs end to end), run it and verify it actually works.
3. Ask me for: GitHub organization or account, repository name, and visibility (private or
   public) before creating the remote with `gh repo create` and pushing.
4. Finish with a short summary: what works, how to verify it, known limitations.

## Additional requirements (these override earlier points where they conflict)

### REST API instead of SOAP
- Use REST only (no SOAP). Use Quarkus REST with Jackson (quarkus-rest, quarkus-rest-jackson),
  JSON as the default format.
- Add OpenAPI documentation and Swagger UI (quarkus-smallrye-openapi), reachable in dev mode at
  /q/swagger-ui, so every team can see and try all endpoints.
- Version all endpoints under /api/v1/...
- Use one consistent error format for all teams, based on RFC 9457 (Problem Details,
  application/problem+json), implemented once in the shared module via exception mappers.
  The project handbook requires a traceable error response for every interface.
- Every request or message between teams must carry: a case ID (Vorgangskennung), a household
  ID (Pflegehaushaltskennung), a timestamp, the sender, and the business payload. Put this
  envelope in the shared module and use it both in REST bodies and JMS messages.
- Write docs/api-konventionen.md (in German) describing these conventions as a PROPOSAL for
  the cross-team integration circle. Do not invent the final shared business model. Only the
  hello-process endpoints are implemented now.
- Provide a Postman collection in /postman with requests for the full hello-process run
  (start process, simulate backend message, query status), plus a matching environment file
  for localhost.

### IDE-independent setup (team uses VS Code, IntelliJ IDEA and Eclipse)
- Maven is the single source of truth. Do not commit IDE project files (.idea/, .project,
  .classpath, .settings/, .vscode/ except as noted below). Add them all to .gitignore.
- Optionally commit only .vscode/extensions.json (recommendations, harmless for others).
- Add .editorconfig (UTF-8, LF line endings, 4-space indentation for Java, 2 for XML/YAML/JSON)
  so formatting is consistent across IDEs.
- Add .gitattributes: `* text=auto eol=lf`, but `*.cmd` and `*.bat` with CRLF, so mvnw and
  mvnw.cmd work on macOS, Linux and Windows.
- Everything must run from the command line without any IDE: ./mvnw quarkus:dev
  (Windows: mvnw.cmd quarkus:dev).
- README section "Entwicklungsumgebung einrichten" with short instructions for each IDE:
  - VS Code: recommended extensions, JDK 17 setting
  - IntelliJ IDEA: open the root pom.xml as a project, set project SDK to 17, Quarkus run
    configuration or ./mvnw quarkus:dev in the terminal
  - Eclipse IDE for Jakarta EE: Import > Existing Maven Projects, set JDK 17 as installed JRE,
    optional Quarkus Tools from the Eclipse Marketplace
  - For all IDEs: how to attach the debugger to Quarkus dev mode (remote debug on port 5005)
- README prerequisites must cover macOS, Windows and Linux (JDK 17, Docker Desktop, Git).
