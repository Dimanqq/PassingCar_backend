package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteRideStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        map.put("ride_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "DELETE FROM ride " +
                "WHERE ride.id = ?::uuid";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("ride_id"));
        return Map.oneValue("status", statement.execute());
    }
}
