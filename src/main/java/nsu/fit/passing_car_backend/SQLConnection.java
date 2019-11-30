package nsu.fit.passing_car_backend;

import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;

import java.io.InputStream;
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

    public boolean auth(String userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT \"user\".id FROM \"user\" WHERE \"user\".id::text = ?")) {
            statement.setString(1, userId);
            return statement.executeQuery().next();
        }
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
        } catch (PSQLException e){
            throw new DataError(10, e.getServerErrorMessage().toString());
        }
    }

    public JSONObject getUser(String userId) throws SQLException {
        JSONObject user;
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT \"user\".id FROM \"user\" WHERE user.id = ?")) {
            statement.setString(1, userId);
            ResultSet res = statement.executeQuery();   //  todo user might be not found
            user = new JSONObject();
            user.put("email", res.getString(1));
            user.put("password", res.getString(2));
            user.put("first_name", res.getString(3));
            user.put("last_name", res.getString(4));
            user.put("phone", res.getString(5));
            return user;
        }
    }

    public ImageData getImage(String imageId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT image.mime_type, image.data FROM image WHERE image.id::text = ?")) {
            statement.setString(1, imageId);
            ResultSet res = statement.executeQuery();   //  todo image might be not found
            res.next();
            ImageData imageData = new ImageData();
            imageData.mimeType = res.getString(1);
            imageData.data = res.getBinaryStream(2);
            return imageData;
        }
    }

    public String createRide(Double lonStart, Double latStart, Double lonFinish, Double latFinish, String time, Integer placesFree, String creatorId) throws SQLException {
        String startId = insertPoint(lonStart, latStart);
        String finishId = insertPoint(lonFinish, latFinish);

        try (PreparedStatement statement = connection.prepareStatement
                (" INSERT INTO \"ride\" (point_start, point_end, time_start, places_count, creator_id) VALUES (?::uuid, ?::uuid, ?::timestamptz, ?, ?::uuid) RETURNING id")) {
            statement.setString(1, startId);
            statement.setString(2, finishId);
            statement.setString(3, time);
            statement.setInt(4, placesFree);
            statement.setString(5, creatorId);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getString(1);
        }
    }

    private String insertPoint(Double lonFinish, Double latFinish) throws SQLException {
        String id;
        try (PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO \"point\" (lat, lon) VALUES (?, ?) RETURNING id")) {
            statement.setDouble(1, latFinish);
            statement.setDouble(2, lonFinish);
            ResultSet res = statement.executeQuery();
            res.next();
            id = res.getString(1);
        }
        return id;
    }


    public int joinRide(String userId, String rideId) throws SQLException {
        int count, places;
        //check repeatable invite query
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT COUNT(*) FROM m2m_ride_user WHERE " +
                        "m2m_ride_user.user_id::text = ? AND m2m_ride_user.ride_id::text = ?")) {
            statement.setString(1, userId);
            statement.setString(2, rideId);
            ResultSet res = statement.executeQuery();
            res.next();
            count = Integer.parseInt(res.getString(1));
            if (count > 0)
                return -1;
        }

        //check count of users who already take part in this trip
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT COUNT (*) FROM m2m_ride_user WHERE m2m_ride_user.ride_id::text = ?")) {
            statement.setString(1, rideId);
            ResultSet res = statement.executeQuery();
            res.next();
            count = Integer.parseInt(res.getString(1));
        }

        //get max count of users in this pass
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT ride.places_count FROM ride WHERE ride.id::text = ?")) {
            statement.setString(1, rideId);
            ResultSet res = statement.executeQuery();
            res.next();
            places = Integer.parseInt(res.getString(1));
        }

        //if this user is first
        try (PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO \"m2m_ride_user\" (user_id, ride_id) VALUES (?::uuid, ?::uuid)")) {
            statement.setString(1, userId);
            statement.setString(2, rideId);
            statement.execute();
        }

        //count with new user
        count++;

        return places - count;
    }
}
