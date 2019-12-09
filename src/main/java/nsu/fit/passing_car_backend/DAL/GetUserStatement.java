package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT" +
                "\"user\".email," +
                "\"user\".password," +
                "\"user\".first_name," +
                "\"user\".last_name," +
                "\"user\".phone" +
                "FROM \"user\" WHERE \"user\".id = ?";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("user_id"));
        Map user;
        try (ResultSet res = statement.executeQuery()) {
            if (!res.next()) {
                throw new DataError(DataError.NOT_FOUND, "User not found");
            }
            user = new Map();
            user.put("email", res.getString(1));
            user.put("password", res.getString(2));
            user.put("first_name", res.getString(3));
            user.put("last_name", res.getString(4));
            user.put("phone", res.getString(5));
        }
        return user;
    }
}
