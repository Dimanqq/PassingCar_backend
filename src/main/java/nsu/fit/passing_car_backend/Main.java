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
        creds.ip = "3.19.71.72";
        creds.port = 5433;
        creds.db = "passing_car";
        creds.user = "postgres";
        creds.password = "qp~pq234";
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
                .post("/create/user", new BlockingHandler(new RegistrationHandler(serverUtils)
                ))
                .post("/signin", new BlockingHandler(new SigninHandler(serverUtils)
                ))
                .post("/rides/{id}/invite", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new InviteRideHandler(serverUtils))
                ))
                .post("/create/ride", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new CreateRideHandler(serverUtils))
                ))
                .get("/users/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetUserHandler(serverUtils))
                ))
                .get("/images/{id}", /*new AuthorizationHandler(*/
                        //     serverUtils,
                        new BlockingHandler(new GetImageHandler(serverUtils))
                )//)
                .get("/riders/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetRideHandler(serverUtils))
                ))
                .get("/search", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new SearchHandler(serverUtils))
                ))
                .delete("/rides/{id}/invite", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new DeleteInviteHandler(serverUtils))
                ));

        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(mainHandler).build();
        server.start();
    }
}
