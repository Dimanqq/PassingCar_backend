package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.BlockingHttpExchange;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CreationHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public CreationHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        BlockingHttpExchange exchange2 = exchange.startBlocking();
        if(exchange2 == null){
            exchange.getResponseSender().send("null");
            return;
        }
        Reader reader = new BufferedReader(new InputStreamReader(exchange2.getInputStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        JSONObject o = (JSONObject) jsonParser.parse(reader);
        String name = (String) o.get("name");
        Long cnt = (Long) o.get("cnt");
        exchange.getResponseSender().send(name + " " + cnt);
    }
}
