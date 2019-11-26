package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;

public class InviteRideHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public InviteRideHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {

    }
}
