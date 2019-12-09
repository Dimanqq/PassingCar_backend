package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class SearchHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public SearchHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        SQLStatement.Map data = SQLStatement.Map.fromExchangeQuery(exchange);
        data.put("user_id", exchange.getRequestHeaders().get("Authorization").getFirst());
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(data.toJSON().toString());
    }
}
