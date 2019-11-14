package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserListHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public UserListHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseSender().send(serverUtils.sqlConnection.getUserList().toString());
    }
}
