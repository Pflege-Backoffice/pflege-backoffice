<!--
  Pflege-Backoffice MVP – SPDX-License-Identifier: MIT
  Verantwortlich: <NAME>
  Hauptautor: KI-generiert (Claude Code, Anthropic)
  KI-GENERIERT START (Claude Code, Anthropic)
-->
# Lizenzen von Drittkomponenten

Unser eigener Code steht unter der **MIT-Lizenz** (siehe `LICENSE`). Das Repository enthält **keine**
Fremdbibliotheken als Dateien; Maven lädt sie beim Bauen. Wer die gebaute Anwendung weitergibt,
gibt diese Bibliotheken mit weiter – deshalb wurde jede Lizenz auf Verträglichkeit mit einer
MIT-Veröffentlichung inkl. kommerzieller Nutzung geprüft.

Stand: 2026-11-16, Quarkus-Plattform 3.27.6, CIB seven 2.2.0, Esper 9.0.0.
Erzeugt aus den Maven-POMs (vollständige Liste unten), Sonderfälle zusätzlich an der Quelle geprüft.

## Bewertung

| Lizenz | Anzahl | Beispiele | Verträglich mit MIT (auch kommerziell)? |
|---|---|---|---|
| Apache License 2.0 | 223 | Quarkus, CIB seven, Apache Camel, ActiveMQ-Client, Hibernate ORM 7, Narayana, Jackson, pooled-jms | ✅ ja |
| MIT / MIT-0 | 2 | SLF4J, reactive-streams | ✅ ja |
| BSD-2-Clause / BSD-3-Clause | 5 | ANTLR 4 Runtime, Janino, Stax2 API, CRaC | ✅ ja |
| Public Domain | 1 | JBoss Transaction SPI | ✅ ja |
| Eclipse Distribution License 1.0 (BSD-artig) | 7 | Jakarta Activation/XML Binding, JAXB Runtime | ✅ ja |
| EPL 2.0 **oder** GPL 2.0 mit Classpath-Exception (Wahl) | 10 | Jakarta Annotations, EL, JMS, JSON, REST, Transaction APIs | ✅ ja (unverändert genutzt; EPL 2.0 wählbar) |
| EPL 1.0/2.0 **oder** Apache 2.0 (Wahl) | 6 | Vert.x Core, Vert.x Web | ✅ ja (Apache 2.0 wählbar) |
| EDL 1.0 **oder** EPL 2.0 (Wahl) | 1 | Jakarta Persistence API | ✅ ja |
| **GPL v2 mit Universal FOSS Exception 1.0** | 1 | **MySQL Connector/J** 8.3.0 | ⚠️ ja für Open Source unter MIT; **nicht** für geschlossene kommerzielle Weitergabe → `docs/offene-punkte.md` OP-002 |
| **GPL v2 (ohne Ausnahme)** | 3 | **Esper** common/compiler/runtime 9.0.0 | ❌ **nicht eindeutig verträglich** → `docs/offene-punkte.md` OP-001 (mit Prof. klären) |

### Esper (GPL v2)

- Quelle: <https://www.espertech.com/esper/esper-license/> – „Esper and NEsper are open-source software
  available under the GNU General Public License (GPLv2).“ POM: „GNU General Public License Version 2“,
  keine Classpath-Exception.
- Esper ist durch den Kurs vorgegeben und wurde **nicht** ersetzt. Es ist im Modul `cep-esper`
  gekapselt. Entscheidung offen: siehe `docs/offene-punkte.md`, OP-001.

### MySQL Connector/J (GPL v2 + Universal FOSS Exception)

- Quelle: POM `com.mysql:mysql-connector-j`; Ausnahmetext <https://oss.oracle.com/licenses/universal-foss-exception/>.
- Die Ausnahme erlaubt die Kombination mit OSI-lizenzierter Software (MIT) und die Weitergabe unter
  getrennten Lizenzen. Einschränkung für proprietäre Nutzung: siehe OP-002.

## Nur zur Laufzeit/Entwicklung genutzt, nicht Teil der Anwendung

| Komponente | Lizenz | Hinweis |
|---|---|---|
| MySQL Server 8.4 (Docker-Image `mysql:8.4`) | GPL v2 | nur als separater Container, wird nicht mit ausgeliefert |
| ActiveMQ Classic 6.2.0 (Docker-Image `apache/activemq-classic`) | Apache 2.0 | separater Container |
| Maven Wrapper 3.3.4 (`mvnw`, `mvnw.cmd`, `.mvn/wrapper/`) | Apache 2.0 | von Apache Maven erzeugt, unverändert |
| Testbibliotheken: JUnit 5, REST Assured, Awaitility, Hamcrest | EPL 2.0 / Apache 2.0 / BSD | nur für Tests, nicht in der Anwendung |

## Liste aktualisieren

Bei jeder neuen Abhängigkeit Liste neu erzeugen und Bewertung oben prüfen:

```bash
./mvnw -B -DskipTests install
./mvnw -B -pl app org.codehaus.mojo:license-maven-plugin:2.5.0:add-third-party \
  -Dlicense.excludedScopes=test,provided -Dlicense.excludedGroups=de.thi.pflegebackoffice
# Ergebnis: app/target/generated-sources/license/THIRD-PARTY.txt
```

## Vollständige Liste (Laufzeit-Abhängigkeiten der Anwendung)

<!-- KI-GENERIERT ENDE (Claude Code, Anthropic) -->
<!-- Die folgende Liste ist automatisch erzeugt (license-maven-plugin), nicht von Hand bearbeiten. -->
```text
Lists of 259 third-party dependencies.
(Apache License 2.0) Brotli4j (com.aayushatharva.brotli4j:brotli4j:1.23.0 - https://github.com/hyperxpro/Brotli4j/brotli4j)
(Apache License 2.0) Brotli4j (com.aayushatharva.brotli4j:native-osx-aarch64:1.23.0 - https://github.com/hyperxpro/Brotli4j/natives/native-osx-aarch64)
(Apache License 2.0) Brotli4j (com.aayushatharva.brotli4j:service:1.23.0 - https://github.com/hyperxpro/Brotli4j/service)
(GNU General Public License Version 2) Esper Common (com.espertech:esper-common:9.0.0 - http://www.espertech.com/esper)
(GNU General Public License Version 2) Esper Compiler (com.espertech:esper-compiler:9.0.0 - http://www.espertech.com/esper)
(GNU General Public License Version 2) Esper Runtime (com.espertech:esper-runtime:9.0.0 - http://www.espertech.com/esper)
(Apache License, Version 2.0) ClassMate (com.fasterxml:classmate:1.7.0 - https://github.com/FasterXML/java-classmate)
(The Apache Software License, Version 2.0) Jackson-annotations (com.fasterxml.jackson.core:jackson-annotations:2.21 - https://github.com/FasterXML/jackson)
(The Apache Software License, Version 2.0) Jackson-core (com.fasterxml.jackson.core:jackson-core:2.21.7 - https://github.com/FasterXML/jackson-core)
(The Apache Software License, Version 2.0) jackson-databind (com.fasterxml.jackson.core:jackson-databind:2.21.7 - https://github.com/FasterXML/jackson)
(The Apache Software License, Version 2.0) Jackson-dataformat-XML (com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.21.7 - https://github.com/FasterXML/jackson-dataformat-xml)
(The Apache Software License, Version 2.0) Jackson-dataformat-YAML (com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.21.7 - https://github.com/FasterXML/jackson-dataformats-text)
(The Apache Software License, Version 2.0) Jackson datatype: jdk8 (com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.21.7 - https://github.com/FasterXML/jackson-modules-java8/jackson-datatype-jdk8)
(The Apache Software License, Version 2.0) Jackson datatype: JSR310 (com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.7 - https://github.com/FasterXML/jackson-modules-java8/jackson-datatype-jsr310)
(The Apache Software License, Version 2.0) Jackson module: Jakarta XML Bind Annotations (jakarta.xml.bind) (com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:2.21.7 - https://github.com/FasterXML/jackson-modules-base)
(The Apache Software License, Version 2.0) Jackson-module-parameter-names (com.fasterxml.jackson.module:jackson-module-parameter-names:2.21.7 - https://github.com/FasterXML/jackson-modules-java8/jackson-module-parameter-names)
(The Apache Software License, Version 2.0) Java UUID Generator (com.fasterxml.uuid:java-uuid-generator:4.3.0 - https://github.com/cowtowncoder/java-uuid-generator)
(The Apache License, Version 2.0) Woodstox (com.fasterxml.woodstox:woodstox-core:7.1.1 - https://github.com/FasterXML/woodstox)
(Apache License, Version 2.0) Caffeine cache (com.github.ben-manes.caffeine:caffeine:3.2.3 - https://github.com/ben-manes/caffeine)
(Apache 2.0) error-prone annotations (com.google.errorprone:error_prone_annotations:2.41.0 - https://errorprone.info/error_prone_annotations)
(The GNU General Public License, v2 with Universal FOSS Exception, v1.0) MySQL Connector/J (com.mysql:mysql-connector-j:8.3.0 - http://dev.mysql.com/doc/connector-j/en/)
(Eclipse Distribution License - v 1.0) istack common utility code runtime (com.sun.istack:istack-commons-runtime:4.1.2 - https://projects.eclipse.org/projects/ee4j/istack-commons/istack-commons-runtime)
(Apache-2.0) Apache Commons Codec (commons-codec:commons-codec:1.19.0 - https://commons.apache.org/proper/commons-codec/)
(Apache License 2.0) Agroal API (io.agroal:agroal-api:2.8.2 - https://github.com/agroal)
(Apache License 2.0) Agroal Narayana Integration (io.agroal:agroal-narayana:2.8.2 - https://github.com/agroal)
(Apache License 2.0) Agroal Connection Pool (io.agroal:agroal-pool:2.8.2 - https://github.com/agroal)
(The Apache Software License, Version 2.0) micrometer-commons (io.micrometer:micrometer-commons:1.14.7 - https://github.com/micrometer-metrics/micrometer)
(The Apache Software License, Version 2.0) micrometer-observation (io.micrometer:micrometer-observation:1.14.7 - https://github.com/micrometer-metrics/micrometer)
(Apache License, Version 2.0) Netty/Buffer (io.netty:netty-buffer:4.1.138.Final - https://netty.io/netty-buffer/)
(Apache License, Version 2.0) Netty/Codec (io.netty:netty-codec:4.1.138.Final - https://netty.io/netty-codec/)
(Apache License, Version 2.0) Netty/Codec/DNS (io.netty:netty-codec-dns:4.1.138.Final - https://netty.io/netty-codec-dns/)
(Apache License, Version 2.0) Netty/Codec/HAProxy (io.netty:netty-codec-haproxy:4.1.138.Final - https://netty.io/netty-codec-haproxy/)
(Apache License, Version 2.0) Netty/Codec/HTTP (io.netty:netty-codec-http:4.1.138.Final - https://netty.io/netty-codec-http/)
(Apache License, Version 2.0) Netty/Codec/HTTP2 (io.netty:netty-codec-http2:4.1.138.Final - https://netty.io/netty-codec-http2/)
(Apache License, Version 2.0) Netty/Codec/Socks (io.netty:netty-codec-socks:4.1.138.Final - https://netty.io/netty-codec-socks/)
(Apache License, Version 2.0) Netty/Common (io.netty:netty-common:4.1.138.Final - https://netty.io/netty-common/)
(Apache License, Version 2.0) Netty/Handler (io.netty:netty-handler:4.1.138.Final - https://netty.io/netty-handler/)
(Apache License, Version 2.0) Netty/Handler/Proxy (io.netty:netty-handler-proxy:4.1.138.Final - https://netty.io/netty-handler-proxy/)
(Apache License, Version 2.0) Netty/Resolver (io.netty:netty-resolver:4.1.138.Final - https://netty.io/netty-resolver/)
(Apache License, Version 2.0) Netty/Resolver/DNS (io.netty:netty-resolver-dns:4.1.138.Final - https://netty.io/netty-resolver-dns/)
(The Apache Software License, Version 2.0) Netty/TomcatNative [OpenSSL - Classes] (io.netty:netty-tcnative-classes:2.0.84.Final - https://github.com/netty/netty-tcnative/netty-tcnative-classes/)
(Apache License, Version 2.0) Netty/Transport (io.netty:netty-transport:4.1.138.Final - https://netty.io/netty-transport/)
(Apache License, Version 2.0) Netty/Transport/Native/Unix/Common (io.netty:netty-transport-native-unix-common:4.1.138.Final - https://netty.io/netty-transport-native-unix-common/)
(The Apache License, Version 2.0) Quarkus CXF - Woodstox (io.quarkiverse.cxf:quarkus-cxf-woodstox:3.27.2 - https://github.com/quarkiverse/quarkiverse-cxf)
(Apache License, Version 2.0) Quarkus - Pooled Jms - Runtime (io.quarkiverse.messaginghub:quarkus-pooled-jms:2.8.0 - https://quarkiverse.io)
(The Apache Software License, Version 2.0) Quarkus - Agroal - Runtime (io.quarkus:quarkus-agroal:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - ArC - Runtime (io.quarkus:quarkus-arc:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) Quarkus - Bootstrap - Runner (io.quarkus:quarkus-bootstrap-runner:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Caffeine - Runtime (io.quarkus:quarkus-caffeine:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) Quarkus - Bootstrap - Classloader common utilities (io.quarkus:quarkus-classloader-commons:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Core - Runtime (io.quarkus:quarkus-core:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Credentials - Runtime (io.quarkus:quarkus-credentials:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Datasource - Runtime (io.quarkus:quarkus-datasource:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Datasource - Common (io.quarkus:quarkus-datasource-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Development mode - SPI (io.quarkus:quarkus-development-mode-spi:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) Quarkus - FS Util (io.quarkus:quarkus-fs-util:1.1.0 - https://quarkus.io/)
(The Apache Software License, Version 2.0) Quarkus - Hibernate ORM - Runtime (io.quarkus:quarkus-hibernate-orm:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Hibernate ORM with Panache - Runtime (io.quarkus:quarkus-hibernate-orm-panache:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Hibernate ORM with Panache - Common - Runtime (io.quarkus:quarkus-hibernate-orm-panache-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Hibernate Validator - Runtime (io.quarkus:quarkus-hibernate-validator:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - IDE Launcher (io.quarkus:quarkus-ide-launcher:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Jackson - Runtime (io.quarkus:quarkus-jackson:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - JAXB - Runtime (io.quarkus:quarkus-jaxb:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - JAXP - Runtime (io.quarkus:quarkus-jaxp:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - JDBC - MySQL - Runtime (io.quarkus:quarkus-jdbc-mysql:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - JSON-P - Runtime (io.quarkus:quarkus-jsonp:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Mutiny - Runtime (io.quarkus:quarkus-mutiny:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Narayana JTA - Runtime (io.quarkus:quarkus-narayana-jta:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Netty - Runtime (io.quarkus:quarkus-netty:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Panache - Common - Runtime (io.quarkus:quarkus-panache-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Panache - Hibernate - Common - Runtime (io.quarkus:quarkus-panache-hibernate-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - REST - Runtime (io.quarkus:quarkus-rest:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - REST - Common - Runtime (io.quarkus:quarkus-rest-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - REST - Jackson - Runtime (io.quarkus:quarkus-rest-jackson:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - REST - Jackson Common Bits - Runtime (io.quarkus:quarkus-rest-jackson-common:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Security - Runtime SPI (io.quarkus:quarkus-security-runtime-spi:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - SmallRye Context Propagation - Runtime (io.quarkus:quarkus-smallrye-context-propagation:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - SmallRye Health - Runtime (io.quarkus:quarkus-smallrye-health:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - SmallRye OpenAPI - Runtime (io.quarkus:quarkus-smallrye-openapi:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Swagger UI - Runtime (io.quarkus:quarkus-swagger-ui:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - TLS Registry - Runtime (io.quarkus:quarkus-tls-registry:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - TLS Registry - Runtime SPI (io.quarkus:quarkus-tls-registry-spi:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Transaction Annotations - Runtime (io.quarkus:quarkus-transaction-annotations:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Vert.x - Runtime (io.quarkus:quarkus-vertx:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Vert.x - HTTP - Runtime (io.quarkus:quarkus-vertx-http:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Vert.x Late Bound MDC Provider (io.quarkus:quarkus-vertx-latebound-mdc-provider:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) Quarkus - Virtual Threads - Runtime (io.quarkus:quarkus-virtual-threads:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) ArC - Runtime (io.quarkus.arc:arc:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) RESTEasy Reactive - Runtime (io.quarkus.resteasy.reactive:resteasy-reactive:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) RESTEasy Reactive - Common Runtime (io.quarkus.resteasy.reactive:resteasy-reactive-common:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) RESTEasy Reactive - Common Types (io.quarkus.resteasy.reactive:resteasy-reactive-common-types:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) RESTEasy Reactive - Jackson Support - Runtime (io.quarkus.resteasy.reactive:resteasy-reactive-jackson:3.27.6 - https://github.com/quarkusio/quarkus)
(Apache License, Version 2.0) RESTEasy Reactive - Vert.x Support - Runtime (io.quarkus.resteasy.reactive:resteasy-reactive-vertx:3.27.6 - https://github.com/quarkusio/quarkus)
(The Apache Software License, Version 2.0) quarkus-security (io.quarkus.security:quarkus-security:2.2.1 - http://www.jboss.org)
(Apache License, Version 2.0) Ancillary classes for making third party frameworks to run on top of Vert.x (io.quarkus.vertx.utils:quarkus-vertx-utils:3.27.6 - https://quarkus.io)
(Apache License, Version 2.0) Jandex: Core (io.smallrye:jandex:3.4.0 - https://smallrye.io)
(The Apache Software License, Version 2.0) SmallRye Context Propagation: Core (io.smallrye:smallrye-context-propagation:2.2.1 - https://github.com/smallrye/smallrye-context-propagation)
(The Apache Software License, Version 2.0) SmallRye Context Propagation: API (io.smallrye:smallrye-context-propagation-api:2.2.1 - https://github.com/smallrye/smallrye-context-propagation)
(The Apache Software License, Version 2.0) SmallRye Context Propagation: JTA (io.smallrye:smallrye-context-propagation-jta:2.2.1 - https://github.com/smallrye/smallrye-context-propagation)
(The Apache Software License, Version 2.0) SmallRye Context Propagation: Storage (io.smallrye:smallrye-context-propagation-storage:2.2.1 - https://github.com/smallrye/smallrye-context-propagation)
(Apache License, Version 2.0) SmallRye Fault Tolerance: Vert.x Integration (io.smallrye:smallrye-fault-tolerance-vertx:6.9.4 - https://smallrye.io)
(Apache License, Version 2.0) SmallRye Health: Implementation (io.smallrye:smallrye-health:4.2.0 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Health: API (io.smallrye:smallrye-health-api:4.2.0 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Health: Provided Health Checks (io.smallrye:smallrye-health-provided-checks:4.2.0 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye: OpenAPI Core (io.smallrye:smallrye-open-api-core:4.0.12 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye: OpenAPI Model (io.smallrye:smallrye-open-api-model:4.0.12 - http://smallrye.io)
(Apache License 2.0) SmallRye Certificate Generator - Private Key PEM utilities (io.smallrye.certs:smallrye-private-key-pem-parser:0.9.2 - https://github.com/cescoffier/certificate-generator)
(Apache License, Version 2.0) SmallRye Common: Annotations (io.smallrye.common:smallrye-common-annotation:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Classloader (io.smallrye.common:smallrye-common-classloader:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Constraints (io.smallrye.common:smallrye-common-constraint:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: CPU (io.smallrye.common:smallrye-common-cpu:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Expressions (io.smallrye.common:smallrye-common-expression:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Functions (io.smallrye.common:smallrye-common-function:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: IO (io.smallrye.common:smallrye-common-io:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Net (io.smallrye.common:smallrye-common-net:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: OS (io.smallrye.common:smallrye-common-os:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: References (io.smallrye.common:smallrye-common-ref:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Common: Vert.x Context Utilities (io.smallrye.common:smallrye-common-vertx-context:2.13.9 - http://smallrye.io)
(Apache License, Version 2.0) SmallRye Config: CDI (io.smallrye.config:smallrye-config:3.13.4 - https://smallrye.io)
(Apache License, Version 2.0) SmallRye Config: Common (io.smallrye.config:smallrye-config-common:3.13.4 - https://smallrye.io)
(Apache License, Version 2.0) SmallRye Config: Core (io.smallrye.config:smallrye-config-core:3.13.4 - https://smallrye.io)
(Apache License, Version 2.0) SmallRye Config: Validator (io.smallrye.config:smallrye-config-validator:3.13.4 - https://smallrye.io)
(Apache License, Version 2.0) SmallRye Mutiny - Core library (io.smallrye.reactive:mutiny:2.9.5 - https://smallrye.io/smallrye-mutiny)
(Apache License, Version 2.0) SmallRye Mutiny - Integration with SmallRye Context Propagation (io.smallrye.reactive:mutiny-smallrye-context-propagation:2.9.5 - https://smallrye.io/smallrye-mutiny)
(Apache License, Version 2.0) SmallRye Mutiny Zero JDK Flow / Reactive Streams Adapters (io.smallrye.reactive:mutiny-zero-flow-adapters:1.1.1 - https://smallrye.io)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x Auth Common (io.smallrye.reactive:smallrye-mutiny-vertx-auth-common:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x Bridge Common (io.smallrye.reactive:smallrye-mutiny-vertx-bridge-common:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x Core (io.smallrye.reactive:smallrye-mutiny-vertx-core:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Runtime Helpers (io.smallrye.reactive:smallrye-mutiny-vertx-runtime:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x URI Template (io.smallrye.reactive:smallrye-mutiny-vertx-uri-template:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x Web (io.smallrye.reactive:smallrye-mutiny-vertx-web:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Mutiny - Vert.x Web Common (io.smallrye.reactive:smallrye-mutiny-vertx-web-common:3.23.1 - https://smallrye.io/smallrye-mutiny-vertx-bindings)
(The Apache Software License, Version 2.0) SmallRye Reactive Converters: API (io.smallrye.reactive:smallrye-reactive-converter-api:3.0.3 - https://smallrye.io)
(The Apache Software License, Version 2.0) SmallRye Reactive Converters: Mutiny (io.smallrye.reactive:smallrye-reactive-converter-mutiny:3.0.3 - https://smallrye.io)
(Eclipse Public License - v 1.0) (The Apache Software License, Version 2.0) Vert.x Auth Common (io.vertx:vertx-auth-common:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-ext/vertx-ext-parent/vertx-auth-parent/vertx-auth-common)
(Eclipse Public License - v 1.0) (The Apache Software License, Version 2.0) Vert.x Bridge Common (io.vertx:vertx-bridge-common:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-ext/vertx-ext-parent/vertx-bridge-common)
(Eclipse Public License - v 2.0) (The Apache Software License, Version 2.0) Vert.x Core (io.vertx:vertx-core:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-core)
(Eclipse Public License - v 1.0) (The Apache Software License, Version 2.0) Vert.x URI Template (io.vertx:vertx-uri-template:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-ext/vertx-ext-parent/vertx-uri-template)
(Eclipse Public License - v 2.0) (The Apache Software License, Version 2.0) Vert.x Web (io.vertx:vertx-web:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-ext/vertx-ext-parent/vertx-web-parent/vertx-web)
(Eclipse Public License - v 1.0) (The Apache Software License, Version 2.0) Vert.x Web Common (io.vertx:vertx-web-common:4.5.34 - https://github.com/vert-x3/vertx-parent/vertx-ext/vertx-ext-parent/vertx-web-parent/vertx-web-common)
(EDL 1.0) Jakarta Activation API (jakarta.activation:jakarta.activation-api:2.1.3 - https://github.com/jakartaee/jaf-api)
(EPL 2.0) (GPL2 w/ CPE) Jakarta Annotations API (jakarta.annotation:jakarta.annotation-api:3.0.0 - https://projects.eclipse.org/projects/ee4j.ca)
(Eclipse Public License v. 2.0) (GNU General Public License, version 2 with the GNU Classpath Exception) Jakarta Expression Language API (jakarta.el:jakarta.el-api:6.0.1 - https://projects.eclipse.org/projects/ee4j.el)
(Apache License 2.0) CDI APIs (jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0 - http://cdi-spec.org)
(Apache License 2.0) CDI Language Model (jakarta.enterprise:jakarta.enterprise.lang-model:4.1.0 - https://projects.eclipse.org/projects/ee4j/jakarta.enterprise.cdi-parent/jakarta.enterprise.lang-model)
(The Apache Software License, Version 2.0) Jakarta Dependency Injection (jakarta.inject:jakarta.inject-api:2.0.1 - https://github.com/eclipse-ee4j/injection-api)
(EPL 2.0) (GPL2 w/ CPE) Jakarta Interceptors (jakarta.interceptor:jakarta.interceptor-api:2.2.0 - https://github.com/jakartaee/interceptors)
(Eclipse Public License 2.0) (GNU General Public License, version 2 with the GNU Classpath Exception) Jakarta Messaging API (jakarta.jms:jakarta.jms-api:3.1.0 - https://projects.eclipse.org/projects/ee4j.jms)
(Eclipse Public License 2.0) (GNU General Public License, version 2 with the GNU Classpath Exception) Jakarta JSON Processing API (jakarta.json:jakarta.json-api:2.1.3 - https://github.com/eclipse-ee4j/jsonp)
(Eclipse Distribution License v. 1.0) (Eclipse Public License v. 2.0) Jakarta Persistence API (jakarta.persistence:jakarta.persistence-api:3.2.0 - https://github.com/jakartaee/persistence)
(EPL 2.0) (GPL2 w/ CPE) jakarta.resource API (jakarta.resource:jakarta.resource-api:2.1.0 - https://github.com/eclipse-ee4j/jca-api)
(EPL 2.0) (GPL2 w/ CPE) jakarta.transaction API (jakarta.transaction:jakarta.transaction-api:2.0.1 - https://projects.eclipse.org/projects/ee4j.jta)
(Apache License 2.0) Jakarta Validation API (jakarta.validation:jakarta.validation-api:3.1.1 - https://beanvalidation.org)
(EPL-2.0) (GPL-2.0-with-classpath-exception) Jakarta RESTful WS API (jakarta.ws.rs:jakarta.ws.rs-api:3.1.0 - https://github.com/eclipse-ee4j/jaxrs-api)
(Eclipse Distribution License - v 1.0) Jakarta XML Binding API (jakarta.xml.bind:jakarta.xml.bind-api:4.0.2 - https://github.com/jakartaee/jaxb-api/jakarta.xml.bind-api)
(Apache License, Version 2.0) Joda-Time (joda-time:joda-time:2.14.0 - https://www.joda.org/joda-time/)
(Apache License, Version 2.0) Byte Buddy (without dependencies) (net.bytebuddy:byte-buddy:1.17.6 - https://bytebuddy.net/byte-buddy)
(BSD-3-Clause) ANTLR 4 Runtime (org.antlr:antlr4-runtime:4.13.2 - https://www.antlr.org/antlr4-runtime/)
(Apache-2.0) ActiveMQ :: Client Jakarta (org.apache.activemq:activemq-client-jakarta:5.19.2 - http://activemq.apache.org/activemq-client-jakarta)
(Apache-2.0) Camel :: ActiveMQ 5.x (org.apache.camel:camel-activemq:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-activemq/)
(Apache-2.0) Camel :: API (org.apache.camel:camel-api:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-api/)
(Apache-2.0) Camel :: Base (org.apache.camel:camel-base:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-base/)
(Apache-2.0) Camel :: Base Engine (org.apache.camel:camel-base-engine:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-base-engine/)
(Apache-2.0) Camel :: Bean (org.apache.camel:camel-bean:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-bean/)
(Apache-2.0) Camel :: Cluster (org.apache.camel:camel-cluster:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-cluster/)
(Apache-2.0) Camel :: Component DSL (org.apache.camel:camel-componentdsl:4.14.8 - https://www.apache.org/camel/camel-parent/dsl/camel-componentdsl/)
(Apache-2.0) Camel :: Core Catalog (org.apache.camel:camel-core-catalog:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-catalog/)
(Apache-2.0) Camel :: Core Engine (org.apache.camel:camel-core-engine:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-engine/)
(Apache-2.0) Camel :: Core Languages (org.apache.camel:camel-core-languages:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-languages/)
(Apache-2.0) Camel :: Core Model (org.apache.camel:camel-core-model:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-model/)
(Apache-2.0) Camel :: Core Processor (org.apache.camel:camel-core-processor:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-processor/)
(Apache-2.0) Camel :: Core Reifier (org.apache.camel:camel-core-reifier:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-core-reifier/)
(Apache-2.0) Camel :: Endpoint DSL (org.apache.camel:camel-endpointdsl:4.14.8 - https://www.apache.org/camel/camel-parent/dsl/camel-endpointdsl/)
(Apache-2.0) Camel :: File (org.apache.camel:camel-file:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-file/)
(Apache-2.0) Camel :: Jackson (org.apache.camel:camel-jackson:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-jackson/)
(Apache-2.0) Camel :: Jackson XML (org.apache.camel:camel-jacksonxml:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-jacksonxml/)
(Apache-2.0) Camel :: JMS (org.apache.camel:camel-jms:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-jms/)
(Apache-2.0) Camel :: Main (org.apache.camel:camel-main:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-main/)
(Apache-2.0) Camel :: Management API (org.apache.camel:camel-management-api:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-management-api/)
(Apache-2.0) Camel :: MicroProfile :: Config (org.apache.camel:camel-microprofile-config:4.14.8 - https://www.apache.org/camel/camel-parent/components/camel-microprofile-parent/camel-microprofile-config/)
(Apache-2.0) Camel :: Support (org.apache.camel:camel-support:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-support/)
(Apache-2.0) Camel :: Tooling :: Model (org.apache.camel:camel-tooling-model:4.14.8 - https://www.apache.org/camel/camel-parent/tooling/camel-tooling-model/)
(Apache-2.0) Camel :: Util (org.apache.camel:camel-util:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-util/)
(Apache-2.0) Camel :: Util :: JSon (org.apache.camel:camel-util-json:4.14.8 - https://www.apache.org/camel/camel-parent/tooling/camel-util-json/)
(Apache-2.0) Camel :: XML JAXP Util (org.apache.camel:camel-xml-jaxp-util:4.14.8 - https://www.apache.org/camel/camel-parent/core/camel-xml-jaxp-util/)
(Apache-2.0) Camel Quarkus :: ActiveMQ :: Runtime (org.apache.camel.quarkus:camel-quarkus-activemq:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-activemq-parent/camel-quarkus-activemq)
(Apache-2.0) Camel Quarkus :: Bean :: Runtime (org.apache.camel.quarkus:camel-quarkus-bean:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-bean-parent/camel-quarkus-bean)
(Apache-2.0) Camel Quarkus :: Core :: Runtime (org.apache.camel.quarkus:camel-quarkus-core:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-core/camel-quarkus-core-parent/camel-quarkus-core)
(Apache-2.0) Camel Quarkus :: File :: Runtime (org.apache.camel.quarkus:camel-quarkus-file:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-file-parent/camel-quarkus-file)
(Apache-2.0) Camel Quarkus :: Jackson :: Runtime (org.apache.camel.quarkus:camel-quarkus-jackson:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-jackson-parent/camel-quarkus-jackson)
(Apache-2.0) Camel Quarkus :: JacksonXML :: Runtime (org.apache.camel.quarkus:camel-quarkus-jacksonxml:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-jacksonxml-parent/camel-quarkus-jacksonxml)
(Apache-2.0) Camel Quarkus :: JMS :: Runtime (org.apache.camel.quarkus:camel-quarkus-jms:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions/camel-quarkus-jms-parent/camel-quarkus-jms)
(Apache-2.0) Camel Quarkus :: Support :: Commons Logging :: Runtime (org.apache.camel.quarkus:camel-quarkus-support-commons-logging:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-commons-logging-parent/camel-quarkus-support-commons-logging)
(Apache-2.0) Camel Quarkus :: Support :: Jackson Dataformat XML :: Runtime (org.apache.camel.quarkus:camel-quarkus-support-jackson-dataformat-xml:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-jackson-dataformat-xml-parent/camel-quarkus-support-jackson-dataformat-xml)
(Apache-2.0) Camel Quarkus :: Support :: Spring :: Runtime (org.apache.camel.quarkus:camel-quarkus-support-spring:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-spring-parent/camel-quarkus-support-spring)
(Apache-2.0) Camel Quarkus :: Support :: Spring :: Beans (org.apache.camel.quarkus:camel-quarkus-support-spring-beans:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-spring-parent/camel-quarkus-support-spring-shade/camel-quarkus-support-spring-beans)
(Apache-2.0) Camel Quarkus :: Support :: Spring :: Context (org.apache.camel.quarkus:camel-quarkus-support-spring-context:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-spring-parent/camel-quarkus-support-spring-shade/camel-quarkus-support-spring-context)
(Apache-2.0) Camel Quarkus :: Support :: Spring :: Core (org.apache.camel.quarkus:camel-quarkus-support-spring-core:3.27.5 - http://camel.apache.org/camel-quarkus-poms/camel-quarkus-build-parent/camel-quarkus-extensions-support/camel-quarkus-support-spring-parent/camel-quarkus-support-spring-shade/camel-quarkus-support-spring-core)
(Apache-2.0) Apache Commons Pool (org.apache.commons:commons-pool2:2.12.0 - https://commons.apache.org/proper/commons-pool/)
(The Apache Software License, Version 2.0) FEEL Scala Engine (org.camunda.feel:feel-engine:1.19.3 - http://www.camunda.org/camunda-bpm-release-parent/feel-engine)
(The Apache Software License, Version 2.0) CIB seven - engine (org.cibseven.bpm:cibseven-engine:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - engine - Cdi - Jakarta (org.cibseven.bpm:cibseven-engine-cdi-jakarta:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven DMN - engine (org.cibseven.bpm.dmn:cibseven-engine-dmn:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven DMN - engine FEEL - API (org.cibseven.bpm.dmn:cibseven-engine-feel-api:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven DMN - engine FEEL - JUEL (org.cibseven.bpm.dmn:cibseven-engine-feel-juel:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven DMN - engine FEEL - SCALA (org.cibseven.bpm.dmn:cibseven-engine-feel-scala:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - JUEL (org.cibseven.bpm.juel:cibseven-juel:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - BPMN Model API (org.cibseven.bpm.model:cibseven-bpmn-model:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - CMMN Model API (org.cibseven.bpm.model:cibseven-cmmn-model:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - DMN Model API (org.cibseven.bpm.model:cibseven-dmn-model:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - Xml Model API (org.cibseven.bpm.model:cibseven-xml-model:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven - Quarkus Extension - Engine - Runtime (org.cibseven.bpm.quarkus:cibseven-bpm-quarkus-engine:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven Commons - Logging (org.cibseven.commons:cibseven-commons-logging:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven Commons - Typed Values (org.cibseven.commons:cibseven-commons-typed-values:2.2.0 - https://cibseven.org)
(The Apache Software License, Version 2.0) CIB seven Commons - Utils (org.cibseven.commons:cibseven-commons-utils:2.2.0 - https://cibseven.org)
(BSD-3-Clause) commons-compiler (org.codehaus.janino:commons-compiler:3.1.12 - http://janino-compiler.github.io/commons-compiler/)
(BSD-3-Clause) janino (org.codehaus.janino:janino:3.1.12 - http://janino-compiler.github.io/janino/)
(The BSD 2-Clause License) Stax2 API (org.codehaus.woodstox:stax2-api:4.2.2 - http://github.com/FasterXML/stax2-api)
(BSD-2-Clause) crac (org.crac:crac:1.5.0 - https://github.com/crac/org.crac)
(EDL 1.0) Angus Activation Registries (org.eclipse.angus:angus-activation:2.0.2 - https://github.com/eclipse-ee4j/angus-activation/angus-activation)
(Apache License, Version 2.0) MicroProfile Config API (org.eclipse.microprofile.config:microprofile-config-api:3.1 - https://microprofile.io/project/eclipse/microprofile-config/microprofile-config-api)
(Apache License, Version 2.0) MicroProfile Context Propagation (org.eclipse.microprofile.context-propagation:microprofile-context-propagation-api:1.3 - http://microprofile.io/microprofile-context-propagation-api)
(Apache License, Version 2.0) MicroProfile Health API (org.eclipse.microprofile.health:microprofile-health-api:4.0.1 - https://microprofile.io/project/eclipse/microprofile-health/microprofile-health-api)
(Apache License, Version 2.0) MicroProfile OpenAPI API (org.eclipse.microprofile.openapi:microprofile-openapi-api:4.0.2 - http://microprofile.io/microprofile-openapi-parent/microprofile-openapi-api)
(Apache License, Version 2.0) Eclipse MicroProfile Reactive Streams Operators API (org.eclipse.microprofile.reactive-streams-operators:microprofile-reactive-streams-operators-api:3.0.1 - http://microprofile.io/microprofile-reactive-streams-operators-parent/microprofile-reactive-streams-operators-api)
(Eclipse Public License 2.0) (GNU General Public License, version 2 with the GNU Classpath Exception) Eclipse Parsson (org.eclipse.parsson:parsson:1.1.7 - https://github.com/eclipse-ee4j/parsson/parsson)
(The Apache Software License, Version 2.0) hawtbuf (org.fusesource.hawtbuf:hawtbuf:1.11 - http://hawtbuf.fusesource.org/hawtbuf)
(Eclipse Public License v. 2.0) (GNU General Public License, version 2 with the GNU Classpath Exception) Eclipse Expressly (org.glassfish.expressly:expressly:6.0.0 - https://projects.eclipse.org/projects/ee4j.expressly)
(Eclipse Distribution License - v 1.0) JAXB Core (org.glassfish.jaxb:jaxb-core:4.0.5 - https://eclipse-ee4j.github.io/jaxb-ri/)
(Eclipse Distribution License - v 1.0) JAXB Runtime (org.glassfish.jaxb:jaxb-runtime:4.0.5 - https://eclipse-ee4j.github.io/jaxb-ri/)
(Eclipse Distribution License - v 1.0) TXW2 Runtime (org.glassfish.jaxb:txw2:4.0.5 - https://eclipse-ee4j.github.io/jaxb-ri/)
(Apache License 2.0) Local-only Hibernate Cache for Quarkus (org.hibernate:quarkus-local-cache:0.3.1 - https://github.com/hibernate/quarkus-local-cache)
(Apache License Version 2.0) Hibernate Models (org.hibernate.models:hibernate-models:1.0.1 - https://github.com/hibernate/hibernate-models)
(Apache License, Version 2.0) Hibernate ORM - hibernate-core (org.hibernate.orm:hibernate-core:7.1.36.Final - https://hibernate.org/orm)
(Apache License, Version 2.0) Hibernate ORM - hibernate-graalvm (org.hibernate.orm:hibernate-graalvm:7.1.36.Final - https://hibernate.org/orm)
(Apache License 2.0) Hibernate Validator Engine (org.hibernate.validator:hibernate-validator:9.0.1.Final - https://hibernate.org/validator)
(Public Domain) JBoss Transaction SPI (org.jboss:jboss-transaction-spi:8.0.0.Final - http://www.jboss.org)
(Apache License 2.0) JBoss Invocation API - Jakarta EE Variant (org.jboss.invocation:jboss-invocation:2.0.0.Final - http://www.jboss.org/jboss-invocation)
(Apache License 2.0) Commons Logging to JBoss Logging (org.jboss.logging:commons-logging-jboss-logging:1.0.0.Final - http://www.jboss.org)
(Apache License 2.0) JBoss Logging 3 (org.jboss.logging:jboss-logging:3.6.1.Final - http://www.jboss.org)
(Apache License 2.0) JBoss Log Manager (org.jboss.logmanager:jboss-logmanager:3.1.2.Final - https://jboss.org)
(Apache-2.0) Narayana: ArjunaJTA narayana-jta (org.jboss.narayana.jta:narayana-jta:7.2.2.Final - https://narayana.io/narayana-jta-all/narayana-jta)
(Apache-2.0) Narayana: ArjunaJTS integration (org.jboss.narayana.jts:narayana-jts-integration:7.2.2.Final - https://narayana.io/narayana-jts-all/narayana-jts-integration)
(Apache License 2.0) SLF4J: JBoss Log Manager (org.jboss.slf4j:slf4j-jboss-logmanager:2.0.2.Final - http://www.jboss.org)
(Apache License 2.0) JBoss Threads (org.jboss.threads:jboss-threads:3.9.1 - https://github.com/jbossas/jboss-threads)
(Apache License, Version 2.0) Java Concurrency Tools Core Library (org.jctools:jctools-core:4.0.5 - https://github.com/JCTools)
(The Apache License, Version 2.0) JSpecify annotations (org.jspecify:jspecify:1.0.0 - http://jspecify.org/)
(The Apache License, Version 2.0) PooledJMS Library (org.messaginghub:pooled-jms:3.1.7 - https://github.com/messaginghub/pooled-jms/pooled-jms)
(The Apache Software License, Version 2.0) mybatis (org.mybatis:mybatis:3.5.19 - https://www.mybatis.org/mybatis-3/)
(MIT-0) reactive-streams (org.reactivestreams:reactive-streams:1.0.4 - http://www.reactive-streams.org/)
(MIT) SLF4J API Module (org.slf4j:slf4j-api:2.0.17 - http://www.slf4j.org)
(Apache License, Version 2.0) Spring Beans (org.springframework:spring-beans:6.2.11 - https://github.com/spring-projects/spring-framework)
(Apache License, Version 2.0) Spring Core (org.springframework:spring-core:6.2.11 - https://github.com/spring-projects/spring-framework)
(Apache License, Version 2.0) Spring Commons Logging Bridge (org.springframework:spring-jcl:6.2.11 - https://github.com/spring-projects/spring-framework)
(Apache License, Version 2.0) Spring JMS (org.springframework:spring-jms:6.2.19 - https://github.com/spring-projects/spring-framework)
(Apache License, Version 2.0) Spring Messaging (org.springframework:spring-messaging:6.2.19 - https://github.com/spring-projects/spring-framework)
(Apache License, Version 2.0) Spring Transaction (org.springframework:spring-tx:6.2.19 - https://github.com/spring-projects/spring-framework)
(Apache License 2.0) wildfly-common (org.wildfly.common:wildfly-common:2.0.1 - http://www.jboss.org)
(Apache License, Version 2.0) SnakeYAML (org.yaml:snakeyaml:2.4 - https://bitbucket.org/snakeyaml/snakeyaml)
```
