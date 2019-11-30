package nsu.fit.passing_car_backend;

import org.postgresql.util.PSQLException;

import java.sql.*;

public class SQLConnection {
    private Connection connection;

    SQLConnection(SQLCreds creds) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager
                .getConnection("jdbc:postgresql://" + creds.ip +
                                ":" + creds.port + "/" + creds.db,
                        creds.user, creds.password);
    }

    private void runSQL(String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    void initDB() throws SQLException {
        runSQL("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"");
        runSQL("CREATE TABLE IF NOT EXISTS \"image\" (\n" +
                "\"id\" uuid NOT NULL DEFAULT uuid_generate_v1(),\n" +
                "\"mime_type\" varchar(30) NOT NULL,\n" +
                "\"data\" bytea NOT NULL,\n" +
                "CONSTRAINT \"image_pk\" PRIMARY KEY (\"id\")\n" +
                ")");
        runSQL("CREATE TABLE IF NOT EXISTS \"user\" (\n" +
                "\"email\" varchar(100) NOT NULL UNIQUE,\n" +
                "\"password\" varchar(100) NOT NULL,\n" +
                "\"first_name\" varchar(100) NOT NULL,\n" +
                "\"last_name\" varchar(100) NOT NULL,\n" +
                "\"phone\" varchar(30) NOT NULL UNIQUE,\n" +
                "\"id\" uuid NOT NULL DEFAULT uuid_generate_v1(),\n" +
                "\"avatar_id\" uuid,\n" +
                "CONSTRAINT \"user_pk\" PRIMARY KEY (\"id\"),\n" +
                "CONSTRAINT \"user_fk0\" FOREIGN KEY (\"avatar_id\") REFERENCES \"image\"(\"id\")\n" +
                ")");
        runSQL("CREATE TABLE IF NOT EXISTS point (\n" +
                "id uuid NOT NULL DEFAULT uuid_generate_v1(),\n" +
                "lat float NOT NULL,\n" +
                "lon float NOT NULL,\n" +
                "CONSTRAINT point_pk PRIMARY KEY (id)\n" +
                ")");
        runSQL("CREATE TABLE IF NOT EXISTS ride (\n" +
                "id uuid NOT NULL DEFAULT uuid_generate_v1(),\n" +
                "point_start uuid NOT NULL,\n" +
                "point_end uuid NOT NULL,\n" +
                "time_start timestamptz NOT NULL,\n" +
                "places_count integer NOT NULL,\n" +
                "creator_id uuid NOT NULL,\n" +
                "CONSTRAINT ride_pk PRIMARY KEY (id),\n" +
                "CONSTRAINT ride_point_start FOREIGN KEY (point_start)\n" +
                "REFERENCES point(id),\n" +
                "CONSTRAINT ride_point_end FOREIGN KEY (point_end)\n" +
                "REFERENCES point(id),\n" +
                "CONSTRAINT ride_creator_id FOREIGN KEY (creator_id)\n" +
                "REFERENCES \"user\"(id)\n" +
                ")");
        runSQL("CREATE TABLE IF NOT EXISTS m2m_ride_user (\n" +
                "user_id uuid NOT NULL,\n" +
                "ride_id uuid NOT NULL,\n" +
                "CONSTRAINT m2m_ride_user_user_id FOREIGN KEY (user_id)\n" +
                "REFERENCES \"user\"(id),\n" +
                "CONSTRAINT m2m_ride_user_ride_id FOREIGN KEY (ride_id)\n" +
                "REFERENCES ride(id)\n" +
                ")");
    }

    public SQLStatement.Map runStatement(SQLStatement.Map data, SQLStatement statement) throws DataError {
        return statement.go(connection, data);
    }

    public String registerUser(String firstName, String lastName, String passw, String phone, String email) throws SQLException, DataError {
        try (PreparedStatement statement = connection.prepareStatement
                (" INSERT INTO \"user\" (" +
                        "email, \"password\", first_name, last_name, phone" +
                        ") VALUES (?, ?, ?, ?, ?) RETURNING id")) {
            statement.setString(1, email);
            statement.setString(2, passw);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, phone);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getString(1);
        } catch (PSQLException e) {
            throw new DataError(10, e.getServerErrorMessage().toString());
        }
    }

}
