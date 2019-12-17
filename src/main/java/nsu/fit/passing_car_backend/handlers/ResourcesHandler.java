package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ResourcesHandler implements HttpHandler {
    private static final Logger log = Logger.getLogger(ResourcesHandler.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.setStatusCode(200);
        try {
            Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("index.htm")
                    .transferTo(exchange.getOutputStream());
        } catch (IOException e) {
            log.error("Server error", e);
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("Server error");
        }
    }
}
