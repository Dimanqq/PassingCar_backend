package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.GetImageStatement;
import nsu.fit.passing_car_backend.DAL.GetUserStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class GetUserHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetUserHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            SQLStatement.Map data = SQLStatement.Map.oneValue(
                    "user_id",
                    exchange.getQueryParameters().get("id").getFirst()
            );
            data = serverUtils.sqlConnection.runStatement(data, new GetUserStatement());
            exchange.setStatusCode(200);
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e){
            e.send(exchange);
        }
    }
}
