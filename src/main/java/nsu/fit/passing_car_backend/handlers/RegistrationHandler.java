package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.CreateUserStatement;
import nsu.fit.passing_car_backend.SQLStatement;
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
        SQLStatement.Map data = SQLStatement.Map.fromJSON(
                (JSONObject) new JSONParser().parse(reader)
        );
        data = serverUtils.sqlConnection.runStatement(data, new CreateUserStatement());
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(data.toJSON().toString());
    }
}
