package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;

public class AuthorizationHandler implements HttpHandler {
    private ServerUtils serverUtils;
    private HttpHandler httpHandler;

    public AuthorizationHandler(ServerUtils serverUtils, HttpHandler httpHandler) {
        this.serverUtils = serverUtils;
        this.httpHandler = httpHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String token = exchange.getRequestHeaders().get("Authorization").getFirst();
        if (serverUtils.sqlConnection.auth(token)){
            httpHandler.handleRequest(exchange);
        } else {
            exchange.setStatusCode(403);
            exchange.getResponseSender().send("Unauthorized");
        }
    }
}
