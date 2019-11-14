package nsu.fit.passing_car_backend;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import nsu.fit.passing_car_backend.handlers.CreationHandler;
import nsu.fit.passing_car_backend.handlers.UserListHandler;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final ServerUtils serverUtils;
        SQLCreds creds = new SQLCreds();
        creds.db = "passing_car";
        creds.ip = "192.168.43.17";
        creds.password = "1234";
        creds.port = 5433;
        creds.user = "postgres";
        try {
            serverUtils = new ServerUtils();
            serverUtils.sqlConnection = new SQLConnection(creds);
//            ResultSet rs = c.prepareStatement("SELECT * FROM test_table").executeQuery();
//            rs.next();
//            System.out.println(rs.getString(2));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }

        HttpHandler mainHandler = Handlers.routing()
                .get("/users", new UserListHandler(serverUtils))
                .post("/new_user", new BlockingHandler(new CreationHandler(serverUtils)));

        Undertow server = Undertow.builder().addHttpListener(8080,
                "0.0.0.0").setHandler(mainHandler).build();
        server.start();
    }
}
