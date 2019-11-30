package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.CreateImageStatement;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONObject;

public class CreateImageHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreateImageHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        SQLStatement.Map map = new SQLStatement.Map();
        map.put("mimeType", exchange.getRequestHeaders().get("Content-Type").getFirst());
        map.put("stream", exchange.getInputStream());
        map = serverUtils.sqlConnection.runStatement(map, new CreateImageStatement());
        JSONObject o = new JSONObject();
        o.put("image_id", map.get("result"));
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(o.toString());
    }
}
