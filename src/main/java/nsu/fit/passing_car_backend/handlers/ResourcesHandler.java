package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesHandler implements HttpHandler {

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
            Logger.getAnonymousLogger().log(Level.SEVERE, "Load res error", e);
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("Server error");
        }
    }
}
