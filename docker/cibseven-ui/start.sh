#!/bin/bash
# Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
# Verantwortlich: <NAME>
# Hauptautor: KI-generiert (Claude Code, Anthropic)
# KI-GENERIERT START (Claude Code, Anthropic)
#
# Startskript fuer den Container cibseven-ui (Image cibseven/cibseven:tomcat-2.2.0).
# Passt die Tomcat-Distribution so an, dass sie die Daten der Quarkus-Engine nur ANZEIGT:
#   1. Job-Executor aus (jobExecutorActivate=false): Timer und asynchrone Schritte fuehrt
#      ausschliesslich die Quarkus-Anwendung aus, denn nur dort gibt es unsere Java-Delegates.
#   2. Rechnungs-Beispiel (camunda-invoice) entfernen: es wuerde Prozesse, Instanzen und
#      Demo-Benutzer in unsere Datenbank schreiben.
# Danach wird das normale Startskript des Images aufgerufen (Datenbank aus DB_* Variablen).
set -euo pipefail
cd /camunda

PLATTFORM=conf/bpm-platform.xml
if ! grep -q 'name="jobExecutorActivate"' "$PLATTFORM"; then
  xmlstarlet ed -L \
    -s "//*[local-name()='process-engine']/*[local-name()='properties']" -t elem -n property -v false \
    -i "//*[local-name()='properties']/*[local-name()='property'][not(@name)]" -t attr -n name -v jobExecutorActivate \
    "$PLATTFORM"
fi
grep -q 'name="jobExecutorActivate">false<' "$PLATTFORM" || { echo "jobExecutorActivate konnte nicht gesetzt werden" >&2; exit 1; }
echo "cibseven-ui: Job-Executor abgeschaltet (jobExecutorActivate=false)"

if [ -d webapps/camunda-invoice ]; then
  rm -r webapps/camunda-invoice
  echo "cibseven-ui: Rechnungs-Beispiel entfernt"
fi

exec ./cibseven.sh
# KI-GENERIERT ENDE (Claude Code, Anthropic)
