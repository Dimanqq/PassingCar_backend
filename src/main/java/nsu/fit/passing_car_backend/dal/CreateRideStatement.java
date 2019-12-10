package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateRideStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("start_id", String.class);
        map.put("end_id", String.class);
        map.put("time_start", String.class);
        map.put("places_count", Long.class);
        map.put("creator_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return " INSERT INTO \"ride\"" +
                "(point_start, point_end, time_start, places_count, creator_id)" +
                "VALUES" +
                "(?::uuid, ?::uuid, ?::timestamptz, ?, ?::uuid)" +
                "RETURNING id";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("start_id"));
        statement.setString(2, (String) data.get("end_id"));
        statement.setString(3, (String) data.get("time_start"));
        statement.setInt(4, ((Long) data.get("places_count")).intValue());
        statement.setString(5, (String) data.get("creator_id"));
        try (ResultSet res = statement.executeQuery()) {
            res.next();
            return Map.oneValue("ride_id", res.getString(1));
        }
    }
}
