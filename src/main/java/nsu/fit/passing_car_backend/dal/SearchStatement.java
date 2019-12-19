package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        map.put("lat_start", Double.class);
        map.put("lon_start", Double.class);
        map.put("lat_end", Double.class);
        map.put("lon_end", Double.class);
        map.put("radius_start", Double.class);
        map.put("radius_end", Double.class);
        map.put("time_needed", String.class);
        map.put("time_delta", Double.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT r.id, point_start.lat, point_start.lon, r.time_start,\n" +
                "extract(epoch from current_timestamp - r.time_start) / 3600,\n" +
                "point_end.lat, point_end.lon\n" +
                "FROM ride AS r\n" +
                "JOIN point AS point_start ON point_start.id = r.point_start\n" +
                "JOIN point AS point_end ON point_end.id = r.point_end\n" +
                "JOIN (SELECT\n" +
                "? AS lat_start,\n" +
                "? AS lon_start,\n" +
                "? AS radius_start,\n" +
                "? AS radius_end,\n" +
                "3 AS b,\n" +
                "? AS lat_end,\n" +
                "? AS lon_end,\n" +
                "?::timestamptz AS time_needed,\n" +
                "?::interval AS time_delta\n" +
                ") AS q ON q.b = 3\n" +
                "WHERE\n" +
                "ABS(point_start.lat - q.lat_start) / 180 * PI() * 6371 * 1000 < q.radius_start\n" +
                "AND ABS(point_start.lon - q.lon_start) / 180 * PI() * 6371 * 1000 < q.radius_start\n" +
                "AND ABS(point_end.lat - q.lat_end) / 180 * PI() * 6371 * 1000 < q.radius_end\n" +
                "AND ABS(point_end.lon - q.lon_end) / 180 * PI() * 6371 * 1000 < q.radius_end\n" +
                "AND r.time_start < q.time_needed + q.time_delta AND r.time_start > q.time_needed - q.time_delta";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setDouble(1, (Double) data.get("lat_start"));
        statement.setDouble(2, (Double) data.get("lon_start"));
        statement.setDouble(3, (Double) data.get("radius_start"));
        statement.setDouble(4, (Double) data.get("radius_end"));
        statement.setDouble(5, (Double) data.get("lat_end"));
        statement.setDouble(6, (Double) data.get("lon_end"));
        statement.setString(7, (String) data.get("time_needed"));
        statement.setString(8, String.valueOf(data.get("time_delta")) + " hour");
        try (ResultSet res = statement.executeQuery()) {
            List<Map> lst = new ArrayList<>();
            while (res.next()) {
                Map e = new Map();
                e.put("ride_id", res.getString(1));
                e.put("lat_start", res.getDouble(2));
                e.put("lon_start", res.getDouble(3));
                e.put("time_start", res.getString(4));
                e.put("time_to_departure", res.getDouble(5));
                e.put("lat_end", res.getDouble(6));
                e.put("lon_end", res.getDouble(7));
                lst.add(e);
            }
            return Map.oneValue("rides", lst);
        }
    }
}
