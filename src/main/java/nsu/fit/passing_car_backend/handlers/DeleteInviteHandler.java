package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import nsu.fit.passing_car_backend.DAL.DeleteInviteStatement;
import nsu.fit.passing_car_backend.DAL.GetRideStatement;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

public class DeleteInviteHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public DeleteInviteHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try{
            SQLStatement.Map data = new SQLStatement.Map();
            data.put("ride_id", exchange.getQueryParameters().get("id").getFirst());
            data.put("user_id", exchange.getRequestHeaders().get("Authorization").getFirst());
            data = serverUtils.sqlConnection.runStatement(data, new DeleteInviteStatement());
            exchange.getResponseSender().send(data.toJSON().toString());
        } catch (DataError e){
            e.send(exchange);
        }
    }
}
