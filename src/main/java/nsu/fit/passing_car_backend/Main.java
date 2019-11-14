package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import nsu.fit.passing_car_backend.handlers.AuthorizationHandler;
import nsu.fit.passing_car_backend.handlers.CreateImageHandler;
import nsu.fit.passing_car_backend.handlers.CreationHandler;
import nsu.fit.passing_car_backend.handlers.UserListHandler;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final ServerUtils serverUtils;
        SQLCreds creds = new SQLCreds();
        creds.ip = "192.168.43.17";
        creds.port = 5433;
        creds.db = "passing_car";
        creds.user = "postgres";
        creds.password = "1234";
        try {
            serverUtils = new ServerUtils();
            serverUtils.sqlConnection = new SQLConnection(creds);
            serverUtils.sqlConnection.initDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }

        HttpHandler mainHandler = Handlers.routing()
                .get("/users", new UserListHandler(serverUtils))
                .post("/new_user", new BlockingHandler(new CreationHandler(serverUtils)))
                .post("/create/image", new BlockingHandler(new AuthorizationHandler(
                        serverUtils, new CreateImageHandler(serverUtils)
                )))
                ;

        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(mainHandler).build();
        server.start();
    }
}
