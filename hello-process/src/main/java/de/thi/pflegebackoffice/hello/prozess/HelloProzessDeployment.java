/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.hello.prozess;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.cibseven.bpm.engine.RepositoryService;
import org.cibseven.bpm.quarkus.engine.extension.event.CamundaEngineStartupEvent;

/**
 * Deployt das BPMN-Modell, sobald die CIB-seven-Engine gestartet ist.
 * Jedes Team legt fuer seine Prozesse eine eigene, gleich aufgebaute Klasse an.
 * Unveraenderte Modelle werden dank Duplikatfilter nicht erneut deployt.
 */
@ApplicationScoped
public class HelloProzessDeployment {

    void beimEngineStart(@Observes CamundaEngineStartupEvent ereignis, RepositoryService repositoryService) {
        repositoryService.createDeployment()
                .name("hello-process")
                .enableDuplicateFiltering(true)
                .addClasspathResource("bpmn/hello-process.bpmn")
                .deploy();
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
