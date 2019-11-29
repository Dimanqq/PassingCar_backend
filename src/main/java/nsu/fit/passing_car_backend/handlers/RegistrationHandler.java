package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.ServerUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class RegistrationHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public RegistrationHandler(ServerUtils serverUtils) {
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
        String firstName = (String) o.get("first_name");
        String lastName = (String) o.get("last_name");
        String passw = (String) o.get("password");
        String phone = (String) o.get("phone");
        String email = (String) o.get("email");
        try {
            String id = serverUtils.sqlConnection.registerUser(firstName, lastName, passw, phone, email);
            JSONObject idObject = new JSONObject();
            idObject.put("user_id", id);
            exchange.setStatusCode(201);
            exchange.getResponseSender().send(idObject.toString());
        } catch(DataError e){
            e.send(exchange);// todo need response if data error
        }
    }
}
