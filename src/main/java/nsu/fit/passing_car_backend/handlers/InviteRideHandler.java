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
        String rideId = exchange.getQueryParameters().get("id").getFirst();
        String userId = exchange.getRequestHeaders().get("Authorization").getFirst();
        int places = serverUtils.sqlConnection.joinRide(userId, rideId);
        if (places != -1){
            exchange.setStatusCode(201);
            exchange.getResponseSender().send("{\"result\":\"" + places +"\"}");
        } else {
            exchange.setStatusCode(200);
            exchange.getResponseSender().send("{\"result\":\"exists\"}");
        }
    }
}
