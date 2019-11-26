package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;

public class GetRiderHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetRiderHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {

    }
}
