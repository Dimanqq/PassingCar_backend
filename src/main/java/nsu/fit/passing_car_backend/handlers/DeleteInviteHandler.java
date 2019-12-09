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
            if((Boolean) data.get("status")) {
                exchange.setStatusCode(202);
                exchange.getResponseSender().send(data.toJSON().toString());
            } else {
                throw new DataError(DataError.NOT_FOUND, "User not in ride");
            }
        } catch (DataError e){
            e.send(exchange);
        }
    }
}
