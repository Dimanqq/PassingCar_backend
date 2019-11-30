package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import nsu.fit.passing_car_backend.handlers.*;

import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        final ServerUtils serverUtils;
        SQLCreds creds = new SQLCreds();
        Map<String, String> env = System.getenv();
        creds.port = Integer.valueOf(env.getOrDefault("POSTGRES_PORT", "5432"));
        creds.ip = env.getOrDefault("POSTGRES_IP", "3.19.71.72");
        creds.db = env.getOrDefault("POSTGRES_DATABASE", "passing_car");
        creds.user = env.getOrDefault("POSTGRES_USER", "postgres");
        creds.password = env.getOrDefault("POSTGRES_PASS", "qp~pq234");
        System.out.println(creds.port + " " + creds.ip + " " + creds.db + " " + creds.user + " " + creds.password);
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
