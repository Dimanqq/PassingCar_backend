package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;

public class Main {
    public static void main(String[] args) {
        HttpHandler h1 = Handlers.path()
                .addPrefixPath("/route1", Handlers.path()
                        .addPrefixPath("/branch1",
                                exchange -> {
                                    exchange.getResponseHeaders()
                                            .put(Headers.CONTENT_TYPE, "text/plain");
                                    exchange.getResponseSender().send("Hello from branch1");
                                })
                        .addPrefixPath("/branch2",
                                exchange -> {
                                    exchange.getResponseHeaders()
                                            .put(Headers.CONTENT_TYPE, "text/plain");
                                    exchange.getResponseSender().send("Hello from branch2");
                                }))
                .addPrefixPath("/route2", exchange -> {
                    exchange.getResponseHeaders()
                            .put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello from r2");
                });
        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(h1).build();
        server.start();
    }
}
