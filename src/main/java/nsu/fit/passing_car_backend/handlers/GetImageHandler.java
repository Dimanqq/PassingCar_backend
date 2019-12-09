package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import nsu.fit.passing_car_backend.DAL.GetImageStatement;
import nsu.fit.passing_car_backend.SQLStatement;
import nsu.fit.passing_car_backend.ServerUtils;

import java.io.InputStream;

public class GetImageHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetImageHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        SQLStatement.Map data = SQLStatement.Map.oneValue(
                "image_id",
                exchange.getQueryParameters().get("id").getFirst()
        );
        data = serverUtils.sqlConnection.runStatement(data, new GetImageStatement());
        exchange.setStatusCode(200);
        exchange.getRequestHeaders().put(
                HttpString.tryFromString("Content-Type"),
                (String) data.get("mimeType")
        );
        ((InputStream) data.get("stream")).transferTo(exchange.getOutputStream());
    }
}
