package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONObject;

public class CreateImageHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateImageHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String mimeType = exchange.getRequestHeaders().get("Content-Type").getFirst();
        String id = serverUtils.sqlConnection.createImage(exchange.getInputStream(), mimeType);
        JSONObject o = new JSONObject();
        o.put("image_id", id);
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(o.toString());
    }
}
