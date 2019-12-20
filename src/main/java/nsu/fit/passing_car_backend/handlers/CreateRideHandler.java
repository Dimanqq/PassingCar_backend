package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.dal.AddPointStatement;
import nsu.fit.passing_car_backend.dal.CreateRideStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.InviteRideAddStatement;

public class CreateRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = SQLStatement.Map.fromExchangeJSON(exchange);
            SQLStatement.Map locationStart = new SQLStatement.Map();
            locationStart.put("lat", data.remove("lat_start"));
            locationStart.put("lon", data.remove("lon_start"));
            data.put("start_id",
                    serverUtils.sqlConnection.runStatement(locationStart, new AddPointStatement()).value()
            );

            SQLStatement.Map locationEnd = new SQLStatement.Map();
            locationEnd.put("lat", data.remove("lat_end"));
            locationEnd.put("lon", data.remove("lon_end"));
            data.put("end_id",
                    serverUtils.sqlConnection.runStatement(locationEnd, new AddPointStatement()).value()
            );
            String user_id = exchange.getRequestHeaders().get("Authorization").getFirst();
            data.put("creator_id", user_id);
            data = serverUtils.sqlConnection.runStatement(data, new CreateRideStatement());
            SQLStatement.Map inv = new SQLStatement.Map();
            inv.put("user_id", user_id);
            inv.put("ride_id", data.get("ride_id"));
            serverUtils.sqlConnection.runStatement(inv, new InviteRideAddStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
