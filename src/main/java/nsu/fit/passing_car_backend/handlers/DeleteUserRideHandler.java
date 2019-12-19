package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.DeleteUserRideStatement;

public class DeleteUserRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public DeleteUserRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = new SQLStatement.Map();
            data.put("ride_id", exchange.getQueryParameters().get("ride_id").getFirst());
            data.put("user_id", exchange.getQueryParameters().get("user_id").getFirst());
            data = serverUtils.sqlConnection.runStatement(data, new DeleteUserRideStatement());
            if ((Boolean) data.get("status")) {
                exchange.setStatusCode(202);
                exchange.getResponseSender().send(data.toJSON().toString());
            } else {
                throw new DataError(DataError.NOT_FOUND, "User not in ride");
            }
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
