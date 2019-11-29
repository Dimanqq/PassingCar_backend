package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;

public class GetRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String rideId = exchange.getQueryParameters().get("id").getFirst();
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(serverUtils.sqlConnection.getRide(rideId).toString());
    }
}
