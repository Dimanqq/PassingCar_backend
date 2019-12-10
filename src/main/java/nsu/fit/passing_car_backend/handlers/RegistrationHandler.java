package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.dal.CreateUserStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class RegistrationHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public RegistrationHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = SQLStatement.Map.fromExchangeJSON(exchange);
            data = serverUtils.sqlConnection.runStatement(data, new CreateUserStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
