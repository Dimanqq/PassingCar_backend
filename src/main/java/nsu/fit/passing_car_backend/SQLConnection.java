package nsu.fit.passing_car_backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    }

    public void addUser(String name, int cnt) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO test_table (name, cnt) VALUES (?,?)")) {
            statement.setString(1, name);
            statement.setInt(2, cnt);
            statement.executeUpdate();
        }
    }

    public JSONObject getUserList() throws SQLException {
        JSONObject users;
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT test_table.name, test_table.cnt, test_table.id FROM test_table")) {
            ResultSet res = statement.executeQuery();

            JSONArray usersArr = new JSONArray();
            users = new JSONObject();

            while (res.next()) {
                JSONObject user = new JSONObject();
                user.put("name", res.getString(1));
                user.put("cnt", res.getString(2));
                user.put("id", res.getString(3));
                usersArr.add(user);
            }
            users.put("users", usersArr);
        }
        return users;
    }

    public boolean auth(String userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT \"user\".id FROM \"user\" WHERE \"user\".id::text = ?")) {
            statement.setString(1, userId);
            return statement.executeQuery().next();
        }
    }

    public String createImage(InputStream is, String mimeType) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                (" INSERT INTO image (mime_type, data) VALUES (?, ?) RETURNING id")) {
            statement.setString(1, mimeType);
            statement.setBinaryStream(2, is);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getString(1);
        }
    }

    public String registerUser(String email, String passw, String firstName, String lastName, String phone) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                (" INSERT INTO \"user\" (email, \"password\", first_name, last_name, phone) VALUES (?, ?, ?, ?, ?) RETURNING id")) {
            return insert(email, passw, firstName, lastName, phone, statement);
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

    public String createRide(String start, String finish, String time, String placesFree, String creatorId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement
                (" INSERT INTO \"ride\" (point_start, point_end, time_start, places_count, creator_id) VALUES (?, ?, ?, ?, ?) RETURNING id")) {
            return insert(start, finish, time, placesFree, creatorId, statement);
        }
    }

    private String insert(String start, String finish, String time, String placesFree, String creatorId, PreparedStatement statement) throws SQLException {
        statement.setString(1, start);
        statement.setString(2, finish);
        statement.setString(3, time);
        statement.setString(4, placesFree);
        statement.setString(5, creatorId);
        ResultSet res = statement.executeQuery();
        res.next();
        return res.getString(1);
    }
}
