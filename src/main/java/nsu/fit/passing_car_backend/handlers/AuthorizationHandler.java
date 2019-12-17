package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;
import nsu.fit.passing_car_backend.dal.UserValidateStatement;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorizationHandler implements HttpHandler {
    private ServerUtils serverUtils;
    private HttpHandler httpHandler;

    public AuthorizationHandler(ServerUtils serverUtils, HttpHandler httpHandler) {
        this.serverUtils = serverUtils;
        this.httpHandler = httpHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            HeaderValues headerValues = exchange.getRequestHeaders().get("Authorization");
            if (headerValues == null) {
                throw new DataError(DataError.UNAUTHORIZED, "No token");
            }
            if (!(Boolean) serverUtils.sqlConnection.runStatement(
                    SQLStatement.Map.oneValue("userId", headerValues.getFirst()),
                    new UserValidateStatement()
            ).value()) {
                throw new DataError(DataError.UNAUTHORIZED, "Invalid token");
            }
            httpHandler.handleRequest(exchange);
        } catch (DataError e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Data error", e);
            e.send(exchange);
        }
    }
}
