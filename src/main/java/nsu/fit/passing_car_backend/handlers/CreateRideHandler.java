package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.AddPointStatement;
import nsu.fit.passing_car_backend.DAL.CreateRideStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class CreateRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            SQLStatement.Map data = SQLStatement.Map.fromExchangeJSON(exchange);
            SQLStatement.Map coor = new SQLStatement.Map();
            coor.put("lat", data.remove("lat_start"));
            coor.put("lon", data.remove("lon_start"));
            data.put("start_id",
                    serverUtils.sqlConnection.runStatement(coor, new AddPointStatement()).value()
            );
            coor.put("lat", data.remove("lat_end"));
            coor.put("lon", data.remove("lon_end"));
            data.put("end_id",
                    serverUtils.sqlConnection.runStatement(coor, new AddPointStatement()).value()
            );
            data.put("creator_id", exchange.getRequestHeaders().get("Authorization").getFirst());
            data = serverUtils.sqlConnection.runStatement(data, new CreateRideStatement());
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e) {
            e.send(exchange);
        }
    }
}
