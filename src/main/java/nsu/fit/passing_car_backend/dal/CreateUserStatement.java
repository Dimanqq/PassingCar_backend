package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateUserStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("first_name", String.class);
        map.put("last_name", String.class);
        map.put("password", String.class);
        map.put("phone", String.class);
        map.put("email", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return " INSERT INTO \"user\" (" +
                "email, \"password\", first_name, last_name, phone" +
                ") VALUES (?, ?, ?, ?, ?) RETURNING id";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("email"));
        statement.setString(2, (String) data.get("password"));
        statement.setString(3, (String) data.get("first_name"));
        statement.setString(4, (String) data.get("last_name"));
        statement.setString(5, (String) data.get("phone"));
        try (ResultSet res = statement.executeQuery()) {
            res.next();
            return Map.oneValue("user_id", res.getString(1));
        }
    }
}
