package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.InviteRideAddStatement;
import nsu.fit.passing_car_backend.dal.InviteRideStatement;

public class InviteRideHandler implements HttpHandler {
    private static final String FREE_PLACES = "free_places";
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
                throw new DataError(DataError.ALREADY_INVITE, "Already invite");
            }
            if (((Integer) res.get(FREE_PLACES)) == 0) {
                throw new DataError(DataError.NO_FREE_PLACES, "No free places");
            }
            serverUtils.sqlConnection.runStatement(data, new InviteRideAddStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(SQLStatement.Map.oneValue(
                    FREE_PLACES,
                    ((Integer) res.get(FREE_PLACES)) - 1
            ).toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
