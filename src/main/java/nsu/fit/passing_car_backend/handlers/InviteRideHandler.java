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

public class InviteRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public InviteRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String userId = exchange.getQueryParameters().get("id").getFirst();

        Reader reader = new BufferedReader(new InputStreamReader(
                exchange.getInputStream(),
                StandardCharsets.UTF_8
        ));
        JSONParser jsonParser = new JSONParser();
        JSONObject o = (JSONObject) jsonParser.parse(reader);
        String rideId = (String) o.get("ride_id");
        if (serverUtils.sqlConnection.joinRide(userId, rideId)){
            exchange.setStatusCode(201);
            exchange.getResponseSender().send("{\"result\":\"ok\"}");
        } else {
            exchange.setStatusCode(200);
            exchange.getResponseSender().send("{\"result\":\"exists\"}");
        }
    }
}
