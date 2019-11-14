package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import nsu.fit.passing_car_backend.handlers.*;

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
                .post("/create/image", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new CreateImageHandler(serverUtils))
                ))
                .post("/create/user", new BlockingHandler(new RegistrationHandler(serverUtils)))
                .post("/create/ride", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new CreateRideHandler(serverUtils))))
                .get("/users/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetUserHandler(serverUtils))
                ))
                .get("/images/{id}", /*new AuthorizationHandler(*/
                        //     serverUtils,
                        new BlockingHandler(new GetImageHandler(serverUtils))
                )//)
                ;

        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(mainHandler).build();
        server.start();
    }
}
