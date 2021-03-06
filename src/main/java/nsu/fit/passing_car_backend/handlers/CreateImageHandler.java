package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.dal.CreateImageStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class CreateImageHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateImageHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map map = new SQLStatement.Map();
            map.put("mimeType", exchange.getRequestHeaders().get("Content-Type").getFirst());
            map.put("stream", exchange.getInputStream());
            map = serverUtils.sqlConnection.runStatement(map, new CreateImageStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(map.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
