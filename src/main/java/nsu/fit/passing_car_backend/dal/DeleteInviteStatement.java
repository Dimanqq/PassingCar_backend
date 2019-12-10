package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteInviteStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        map.put("ride_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "DELETE FROM m2m_ride_user " +
                "WHERE user_id = ?::uuid " +
                "AND ride_id = ?::uuid " +
                "RETURNING user_id";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("user_id"));
        statement.setString(2, (String) data.get("ride_id"));
        try (ResultSet res = statement.executeQuery()) {
            return Map.oneValue("status", res.next());
        }
    }
}
