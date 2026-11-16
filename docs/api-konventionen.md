<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: <NAME>
  Hauptautor: KI-generiert (Claude Code, Anthropic)
  KI-GENERIERT START (Claude Code, Anthropic)
-->
# API-Konventionen – VORSCHLAG für den Integrationskreis

> **Status: Vorschlag.** Dieses Dokument beschreibt technische Konventionen, die im Starter-Repository
> bereits umgesetzt sind. Es legt **kein** fachliches Datenmodell fest – das gemeinsame Geschäftsmodell
> (Pflegehaushalt, Vorgang, Dokument, …) erarbeitet der Integrationskreis. Änderungen bitte per Pull
> Request und Eintrag in `entscheidungsprotokoll.md`.

## 1. Nur REST, JSON als Standard

- Keine SOAP-Schnittstellen.
- Quarkus REST mit Jackson (`quarkus-rest`, `quarkus-rest-jackson`).
- Anfrage- und Antwortformat: `application/json`; Fehler: `application/problem+json`.
- Zeitangaben: ISO 8601 in UTC, z. B. `2026-11-16T10:15:30Z`.
- Feldnamen: deutsch, `camelCase`, ohne Umlaute (`vorgangskennung`, `pflegehaushaltskennung`).

## 2. Versionierte Pfade

```
/api/v1/<bereich>/<ressource>[/<kennung>]
```

| Bereich | Team | Beispiel |
|---|---|---|
| `hello` | Beispiel | `/api/v1/hello/vorgaenge/{vorgangskennung}` |
| `team1` (Vorschlag: `verwaltung`) | Team 1 | `/api/v1/verwaltung/...` |
| `team2` (Vorschlag: `material`) | Team 2 | `/api/v1/material/...` |
| `team3` (Vorschlag: `abwesenheit`) | Team 3 | `/api/v1/abwesenheit/...` |

- Ressourcen im Plural (`vorgaenge`), Aktionen als Unterressource (`.../backend-antworten`).
- `POST` legt an → `201 Created` + `Location`-Header; asynchron angenommen → `202 Accepted`.
- Inkompatible Änderungen → neue Version `/api/v2/...`, alte Version bleibt bis zur Abstimmung.

## 3. Gemeinsamer Nachrichten-Umschlag

Jede Anfrage und jede Nachricht **zwischen Teams** (REST-Body und JMS-Nachricht) verwendet denselben
Umschlag – Klasse `de.thi.pflegebackoffice.shared.nachricht.Umschlag<T>` im Modul `shared`:

```json
{
  "vorgangskennung": "V-DEMO-0001",
  "pflegehaushaltskennung": "H-DEMO-0001",
  "zeitstempel": "2026-11-16T10:15:30Z",
  "absender": "team2-material",
  "nutzdaten": { "…": "fachliche Daten des jeweiligen Aufrufs" }
}
```

| Feld | Pflicht | Bedeutung |
|---|---|---|
| `vorgangskennung` | ja | Kennung des Vorgangs; **Business Key** der Prozessinstanz, Grundlage der Korrelation |
| `pflegehaushaltskennung` | ja | Kennung des Pflegehaushalts |
| `zeitstempel` | ja | Erzeugungszeitpunkt der Nachricht (UTC) |
| `absender` | ja | sendendes Modul/System, z. B. `team1-verwaltung`, `backend-simulator` |
| `nutzdaten` | ja | fachlicher Inhalt; Aufbau je Schnittstelle (vom Integrationskreis festzulegen) |

Als XML (z. B. für Dateischnittstellen) sieht derselbe Umschlag so aus – Umwandlung mit
`UmschlagFormate.json()` / `UmschlagFormate.xml()`:

```xml
<umschlag>
  <vorgangskennung>V-DEMO-0001</vorgangskennung>
  <pflegehaushaltskennung>H-DEMO-0001</pflegehaushaltskennung>
  <zeitstempel>2026-11-16T10:15:30Z</zeitstempel>
  <absender>team2-material</absender>
  <nutzdaten>…</nutzdaten>
</umschlag>
```

Offen für den Integrationskreis: Format der Kennungen (Vorschlag `V-…`, `H-…`), Liste der erlaubten
Absender, ggf. zusätzliche Felder (z. B. `korrelationsId`, `nachrichtentyp`, `version`).

## 4. Einheitliches Fehlerformat (RFC 9457)

Alle Fehler aller Schnittstellen werden zentral in `shared` (`ProblemDetailsMapper`) erzeugt.
Teams werfen einfach eine Exception, am besten `FachlicherFehler`:

```java
throw FachlicherFehler.nichtGefunden("Vorgang " + vk + " existiert nicht.");
throw FachlicherFehler.konflikt("Vorgang " + vk + " existiert bereits.");
throw new FachlicherFehler(422, "freigabe-fehlt", "Freigabe fehlt", "…");
```

Antwort (`Content-Type: application/problem+json`):

```json
{
  "type": "urn:pflege-backoffice:problem:nicht-gefunden",
  "title": "Ressource nicht gefunden",
  "status": 404,
  "detail": "Vorgang V-1 existiert nicht.",
  "instance": "/api/v1/hello/vorgaenge/V-1",
  "fehlerId": "71fe6b6b-89ea-4656-a1da-7cad4d23196f",
  "zeitstempel": "2026-11-16T11:23:53Z"
}
```

- `type`, `title`, `status`, `detail`, `instance`: wie in RFC 9457 definiert.
- `fehlerId`: steht **auch im Server-Log** → jeder Fehler ist nachvollziehbar (Anforderung Projekthandbuch).
- `verletzungen`: bei Validierungsfehlern (`400`) die Liste `[{ "feld": …, "meldung": … }]`.
- Unerwartete Fehler (`500`) geben keine technischen Details preis, nur die `fehlerId`.

## 5. JMS-Konventionen

- Queue-Namen: `<bereich>.<nachricht>`, z. B. `hello.anfrage`, `material.bestellung`.
- Fehler-Queue je Eingangs-Queue: `<queue>.fehler`.
- Inhalt: Umschlag als JSON-Text (`TextMessage`).
- Korrelation mit Prozessen immer über `vorgangskennung` = Business Key.

## 6. Dokumentation

- Jede Ressource bekommt `@Tag`, `@Operation` und `@APIResponse` (auch für Fehlerfälle).
- Swagger UI im Entwicklungsmodus: <http://localhost:8080/q/swagger-ui>, OpenAPI: `/q/openapi`.
<!-- KI-GENERIERT ENDE (Claude Code, Anthropic) -->
