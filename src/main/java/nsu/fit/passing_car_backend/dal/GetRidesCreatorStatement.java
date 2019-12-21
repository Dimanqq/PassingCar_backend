package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRidesCreatorStatement extends SQLStatement {
    @Override
    protected SQLStatement.AssertMap getAssert() {
        SQLStatement.AssertMap map = new SQLStatement.AssertMap();
        map.put("user_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT " +
                "to_json(ride.time_start)#>>'{}', " +
                "ride.places_count, " +
                "ride.creator_id::text  , " +
                "point_start.lat, " +
                "point_start.lon, " +
                "point_end.lat, " +
                "point_end.lon, " +
                "ride.id::text " +
                "FROM ride " +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_start ON point_start.id = ride.point_start " +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_end ON point_end.id = ride.point_end " +
                "WHERE ride.creator_id = ?::uuid";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("user_id"));
        try (ResultSet res = statement.executeQuery()) {
            List<Map> rideList = new ArrayList<>();
            while (res.next()) {
                Map ride = new Map();
                ride.put("time_start", res.getString(1));
                ride.put("places_count", res.getInt(2));
                ride.put("creator_id", res.getString(3));
                ride.put("lat_start", res.getDouble(4));
                ride.put("lon_start", res.getDouble(5));
                ride.put("lat_end", res.getDouble(6));
                ride.put("lon_end", res.getDouble(7));
                ride.put("ride_id", res.getString(8));
                rideList.add(ride);
            }
            Map result = new Map();
            result.put("rides", rideList);
            result.put("user_id", data.get("user_id"));
            return result;
        }
    }
}
