package nsu.fit.passing_car_backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    Connection connection;

    SQLConnection(SQLCreds creds) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager
                .getConnection("jdbc:postgresql://" + creds.ip +
                        ":" + creds.port + "/" + creds.db,
                        creds.user, creds.password);
    }
}
