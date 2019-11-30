package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.AddPointStatement;
import nsu.fit.passing_car_backend.DAL.CreateRideStatement;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CreateRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(
                exchange.getInputStream(),
                StandardCharsets.UTF_8
        ));
        SQLStatement.Map data = SQLStatement.Map.fromJSON((JSONObject) new JSONParser().parse(reader));
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
        System.out.println(data.toJSON().toString());
        data = serverUtils.sqlConnection.runStatement(data, new CreateRideStatement());
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(data.toJSON().toString());
    }
}
