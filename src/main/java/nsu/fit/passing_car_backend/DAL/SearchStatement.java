package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;
import org.json.simple.JSONArray;

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
        return "SELECT ride.id, point.lat, point.lon " +
                "FROM ride " +
                "JOIN point ON point.id = ride.point_start " +
                "JOIN (SELECT ? AS lat, ? AS lon, ? AS radius, 3 AS b) AS q ON q.b = 3 " +
                "WHERE ABS(point.lat - q.lat) / 180 * PI() * 6371 * 1000 < q.radius " +
                "AND ABS(point.lon - q.lon) / 180 * PI() * 6371 * 1000 < q.radius";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        statement.setDouble(1, (Double) data.get("lat"));
        statement.setDouble(2, (Double) data.get("lon"));
        statement.setDouble(3, RADIUS);
        try (ResultSet res = statement.executeQuery()) {
            List<Map> lst = new ArrayList<>();
            while(res.next()){
                Map e = new Map();
                e.put("ride_id", res.getString(1));
                e.put("lat_start", res.getDouble(2));
                e.put("lon_start", res.getDouble(3));
                lst.add(e);
            }
            return Map.oneValue("rides", lst);
        }
    }
}
