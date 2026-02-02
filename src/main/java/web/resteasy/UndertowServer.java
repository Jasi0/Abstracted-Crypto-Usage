package web.resteasy;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;

public class UndertowServer {
    private static final Logger log = LoggerFactory.getLogger(UndertowServer.class);

    public static void main(String[] args) {
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        server.start(
                Undertow.builder()
                        .addHttpListener(8081, "0.0.0.0")
        );
        server.deploy(RestEasyApp.class);
        log.info("RESTEasy Undertow server started on http://localhost:8081");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            log.info("RESTEasy Undertow server stopped");
        }));
    }
}