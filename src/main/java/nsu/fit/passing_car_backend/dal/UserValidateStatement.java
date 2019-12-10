package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserValidateStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        return null;
    }

    @Override
    protected String getSQL() {
        return "SELECT \"user\".id FROM \"user\" WHERE \"user\".id::text = ?";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("userId"));
        try (ResultSet res = statement.executeQuery()) {
            return Map.oneValue(res.next());
        }
    }
}
