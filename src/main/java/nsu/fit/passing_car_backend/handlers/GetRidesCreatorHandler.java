package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.GetRidesCreatorStatement;

public class GetRidesCreatorHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetRidesCreatorHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = new SQLStatement.Map();
            data.put("user_id", exchange.getQueryParameters().get("id").getFirst());
            data = serverUtils.sqlConnection.runStatement(data, new GetRidesCreatorStatement());
            exchange.setStatusCode(200);
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
