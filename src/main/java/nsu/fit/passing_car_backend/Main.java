package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final Connection c;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://10.100.3.11:5433/passing_car", "postgres", "1234");
            ResultSet rs = c.prepareStatement("SELECT * FROM test_table").executeQuery();
            rs.next();
            System.out.println(rs.getString(2));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }
        HttpHandler h1 = Handlers.path()
                .addPrefixPath("/route1", Handlers.path()
                        .addPrefixPath("/branch1",
                                exchange -> {
                                    String name = exchange.getQueryParameters().get("name").getFirst();
                                    String cnt = exchange.getQueryParameters().get("cnt").getFirst();
                                    exchange.getResponseHeaders()
                                            .put(Headers.CONTENT_TYPE, "text/plain");
                                    exchange.getResponseSender().send(name + "    " +  cnt);
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
