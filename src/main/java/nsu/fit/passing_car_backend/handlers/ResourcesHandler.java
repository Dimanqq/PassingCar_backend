package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;

import java.io.IOException;

public class ResourcesHandler implements HttpHandler{

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.setStatusCode(200);
        try {
            Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("index.html")
                    .transferTo(exchange.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("Server error");
        }
    }
}
