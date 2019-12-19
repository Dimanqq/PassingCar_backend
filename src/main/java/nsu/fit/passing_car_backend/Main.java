package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import nsu.fit.passing_car_backend.handlers.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static final private Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        final ServerUtils serverUtils;
        try {
            serverUtils = new ServerUtils();
            serverUtils.sqlConnection = new SQLConnection(SQLCreds.loadFromEnv());
            serverUtils.sqlConnection.initDB();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "DB init", e);
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
                .get("/images/{id}",
                        new BlockingHandler(new GetImageHandler(serverUtils))
                )
                .get("/rides/{id}", new AuthorizationHandler(
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
                ))
                .delete("/rides/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new DeleteRideHandler(serverUtils))
                ))
                .delete("/rides/{ride_id}/{user_id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new DeleteUserRideHandler(serverUtils))
                ))
                .get("/res", new BlockingHandler(new ResourcesHandler()
                ))
                .get("/rides/creator/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetRidesCreatorHandler(serverUtils))
                ))
                .get("/rides/member/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetRidesMemberHandler(serverUtils))
                ))
                .get("/users/member/{id}", new AuthorizationHandler(
                        serverUtils,
                        new BlockingHandler(new GetMembersRideHandler(serverUtils))
                ));

        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(mainHandler).build();
        server.start();
    }
}
