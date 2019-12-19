package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.DeleteRideM2MStatement;
import nsu.fit.passing_car_backend.dal.DeleteRideStatement;

public class DeleteRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public DeleteRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data1 = new SQLStatement.Map();
            data1.put("ride_id", exchange.getQueryParameters().get("id").getFirst());
            data1.put("user_id", exchange.getRequestHeaders().get("Authorization").getFirst());

            SQLStatement.Map data2 = new SQLStatement.Map();
            data2.put("ride_id", exchange.getQueryParameters().get("id").getFirst());
            data2.put("user_id", exchange.getRequestHeaders().get("Authorization").getFirst());

            serverUtils.sqlConnection.runStatement(data1, new DeleteRideM2MStatement());
            data2 = serverUtils.sqlConnection.runStatement(data2, new DeleteRideStatement());
            exchange.setStatusCode(202);
            exchange.getResponseSender().send(data2.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
