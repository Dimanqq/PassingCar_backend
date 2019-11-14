package nsu.fit.passing_car_backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        runSQL("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS test_table (\n" +
                "id UUID NOT NULL DEFAULT uuid_generate_v1(),\n" +
                "name VARCHAR(100),\n" +
                "cnt INTEGER,\n" +
                "CONSTRAINT test_table_id PRIMARY KEY (id)\n" +
                ");");
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
}
