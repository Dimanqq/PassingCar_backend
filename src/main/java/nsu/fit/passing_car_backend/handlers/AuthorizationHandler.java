package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import nsu.fit.passing_car_backend.DAL.UserValidateStatement;
import nsu.fit.passing_car_backend.SQLStatement;
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
        HeaderValues headerValues = exchange.getRequestHeaders().get("Authorization");
        if (headerValues == null) {
            exchange.setStatusCode(401);
            exchange.getResponseSender().send("No token");
            return;
        }
        if ((Boolean) serverUtils.sqlConnection.runStatement(
                SQLStatement.Map.oneValue("userId", headerValues.getFirst()),
                new UserValidateStatement()
        ).value()) {
            httpHandler.handleRequest(exchange);
        } else {
            exchange.setStatusCode(403);
            exchange.getResponseSender().send("Unauthorized");
        }
    }
}
