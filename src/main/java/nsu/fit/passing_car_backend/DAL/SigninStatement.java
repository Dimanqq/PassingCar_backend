package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SigninStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("email", String.class);
        map.put("password", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT id FROM \"user\" WHERE email = ? AND password = ?";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("email"));
        statement.setString(2, (String) data.get("password"));
        try (ResultSet res = statement.executeQuery()) {
            if (!res.next()) {
                throw new DataError(DataError.NOT_FOUND, "User not found");
            }
            return Map.oneValue("user_id", res.getString(1));
        }
    }
}
