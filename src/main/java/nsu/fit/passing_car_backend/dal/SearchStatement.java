package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchStatement extends SQLStatement {
    private static final double RADIUS = 10; // meters

    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        map.put("lat", Double.class);
        map.put("lon", Double.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT ride.id, point_start.lat, point_start.lon, ride.time_start " +
                "FROM ride " +
                "JOIN point AS point_start ON point_start.id = ride.point_start " +
                "JOIN point AS point_end ON point_end.id = ride.point_end " +
                "JOIN (SELECT " +
                "? AS lat_start, " +
                "? AS lon_start, " +
                "? AS radius, " +
                "3 AS b, " +
                "? AS lat_end, " +
                "? AS lon_end, " +
                "?::timestamptz AS time_needed, " +
                "?::interval AS time_delta" +
                ") AS q ON q.b = 3 " +
                "WHERE " +
                "ABS(point_start.lat - q.lat_start) / 180 * PI() * 6371 * 1000 < q.radius " +
                "AND ABS(point_start.lon - q.lon_start) / 180 * PI() * 6371 * 1000 < q.radius " +
                "AND ABS(point_end.lat - q.lat_end) / 180 * PI() * 6371 * 1000 < q.radius " +
                "AND ABS(point_end.lon - q.lon_end) / 180 * PI() * 6371 * 1000 < q.radius " +
                "AND r.time_start < q.time_needed + q.time_delta AND r.time_start > q.time_needed - q.time_delta";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setDouble(1, (Double) data.get("lat_start"));
        statement.setDouble(2, (Double) data.get("lon_start"));
        statement.setDouble(4, (Double) data.get("lat_end"));
        statement.setDouble(5, (Double) data.get("lon_end"));
        statement.setDouble(3, (Double) data.get("radius"));

        statement.setString(6, (String) data.get("time_needed"));
        statement.setString(7, String.valueOf(data.get("time_delta")) + " hour");
        try (ResultSet res = statement.executeQuery()) {
            List<Map> lst = new ArrayList<>();
            while (res.next()) {
                Map e = new Map();
                e.put("ride_id", res.getString(1));
                e.put("lat_start", res.getDouble(2));
                e.put("lon_start", res.getDouble(3));
                e.put("time_start", res.getString(4));
                lst.add(e);
            }
            return Map.oneValue("rides", lst);
        }
    }
}
