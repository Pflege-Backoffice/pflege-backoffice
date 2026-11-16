/*
 * Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
 * Verantwortlich: <NAME>
 * Hauptautor: KI-generiert (Claude Code, Anthropic)
 */
// KI-GENERIERT START (Claude Code, Anthropic)
package de.thi.pflegebackoffice.cep;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.EPDeployException;
import com.espertech.esper.runtime.client.EPDeployment;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPStatement;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Duenne Huelle um die Esper-Engine. Andere Module benutzen NUR diese Klasse,
 * nie Esper direkt – so bleibt die GPL-Abhaengigkeit an einer Stelle gekapselt
 * und koennte spaeter ausgetauscht werden.
 * <p>
 * Ereignisse sind einfache Maps (Name -> Wert); ihr Aufbau wird im EPL mit
 * {@code create schema ...} beschrieben.
 */
@ApplicationScoped
public class EsperDienst {

    private static final Logger LOG = Logger.getLogger(EsperDienst.class);

    private Configuration konfiguration;
    private EPRuntime runtime;

    @PostConstruct
    void starten() {
        konfiguration = new Configuration();
        runtime = EPRuntimeProvider.getRuntime("pflege-backoffice", konfiguration);
        runtime.initialize();
        LOG.info("Esper-Runtime gestartet");
    }

    @PreDestroy
    void stoppen() {
        runtime.destroy();
    }

    /**
     * Uebersetzt EPL-Regeln, installiert sie und ruft {@code reaktion} fuer jedes Ergebnis
     * der Regel mit dem angegebenen {@code @name(...)} auf.
     */
    public void regelInstallieren(String epl, String regelName, Consumer<Map<String, Object>> reaktion) {
        try {
            CompilerArguments argumente = new CompilerArguments(konfiguration);
            argumente.getPath().add(runtime.getRuntimePath());
            EPCompiled uebersetzt = EPCompilerProvider.getCompiler().compile(epl, argumente);
            EPDeployment deployment = runtime.getDeploymentService().deploy(uebersetzt);

            for (EPStatement statement : deployment.getStatements()) {
                if (regelName.equals(statement.getName())) {
                    statement.addListener((neu, alt, stmt, rt) -> {
                        for (EventBean ereignis : neu) {
                            reaktion.accept(alsMap(ereignis));
                        }
                    });
                }
            }
            LOG.infof("Esper-Regel '%s' installiert", regelName);
        } catch (EPCompileException | EPDeployException e) {
            throw new IllegalStateException("Esper-Regel '" + regelName + "' ist ungueltig", e);
        }
    }

    /** Schickt ein Ereignis vom Typ {@code ereignisTyp} (siehe create schema) an Esper. */
    public void ereignisSenden(String ereignisTyp, Map<String, Object> daten) {
        runtime.getEventService().sendEventMap(daten, ereignisTyp);
    }

    private static Map<String, Object> alsMap(EventBean ereignis) {
        Map<String, Object> werte = new LinkedHashMap<>();
        for (String name : ereignis.getEventType().getPropertyNames()) {
            werte.put(name, ereignis.get(name));
        }
        return werte;
    }
}
// KI-GENERIERT ENDE (Claude Code, Anthropic)
