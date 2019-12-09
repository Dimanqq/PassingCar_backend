package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.InviteRideAddStatement;
import nsu.fit.passing_car_backend.DAL.InviteRideStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class InviteRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public InviteRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = new SQLStatement.Map();
            data.put("ride_id", exchange.getQueryParameters().get("id").getFirst());
            data.put("user_id", exchange.getRequestHeaders().get("Authorization").getFirst());
            SQLStatement.Map res = serverUtils.sqlConnection.runStatement(data, new InviteRideStatement());
            if (((Boolean) res.get("already_invite"))) {
                throw new DataError(DataError.ALREADY_INVITE, "");
            }
            if (((Integer) res.get("free_places")) == 0) {
                throw new DataError(DataError.NO_FREE_PLACES, "");
            }
            serverUtils.sqlConnection.runStatement(data, new InviteRideAddStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(SQLStatement.Map.oneValue(
                    "free_places",
                    ((Integer) res.get("free_places")) - 1
            ).toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
