package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
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
        JSONParser jsonParser = new JSONParser();
        JSONObject o = (JSONObject) jsonParser.parse(reader);
        Double latStart = (Double) o.get("lat_start");
        Double lonStart = (Double) o.get("lon_start");
        Double latEnd = (Double) o.get("lat_end");
        Double lonEnd = (Double) o.get("lon_end");
        String timeStart = (String) o.get("time_start");
        Integer placesCount = ((Long)o.get("places_count")).intValue();
        String creatorId = exchange.getRequestHeaders().get("Authorization").getFirst();
        String rideId = serverUtils.sqlConnection.createRide(lonStart, latStart, lonEnd, latEnd, timeStart, placesCount, creatorId);
        JSONObject idObject = new JSONObject();
        idObject.put("ride_id", rideId);
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(idObject.toString());
    }
}
