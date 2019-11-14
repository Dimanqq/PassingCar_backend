package nsu.fit.passing_car_backend.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import nsu.fit.passing_car_backend.ImageData;
import nsu.fit.passing_car_backend.ServerUtils;

public class GetImageHandler implements HttpHandler {
    private ServerUtils serverUtils;

    public GetImageHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String imageId = exchange.getQueryParameters().get("id").getFirst();
        ImageData imageData = serverUtils.sqlConnection.getImage(imageId);
        exchange.setStatusCode(200);
        exchange.getRequestHeaders().put(HttpString.tryFromString("Content-Type"), imageData.mimeType);
        imageData.data.transferTo(exchange.getOutputStream());
    }
}
