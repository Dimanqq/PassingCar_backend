package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRidesMemberStatement extends SQLStatement {
    @Override
    protected SQLStatement.AssertMap getAssert() {
        SQLStatement.AssertMap map = new SQLStatement.AssertMap();
        map.put("user_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT " +
                "to_json(r.time_start)#>>'{}', " +
                "r.places_count, " +
                "r.creator_id::text, " +
                "r.id::text, " +
                "point_start.lat, " +
                "point_start.lon, " +
                "point_end.lat, " +
                "point_end.lon, " +
                "m2m_ride_user.user_id::text " +
                "FROM m2m_ride_user " +
                "JOIN (SELECT ride.time_start, ride.places_count, ride.creator_id, ride.id, ride.point_start, ride.point_end FROM ride) AS r ON m2m_ride_user.ride_id = r.id " +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_start ON point_start.id = r.point_start " +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_end ON point_end.id = r.point_end " +
                "WHERE m2m_ride_user.user_id = ?::uuid";
    }

    @Override
    protected SQLStatement.Map run(PreparedStatement statement, SQLStatement.Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("user_id"));
        try (ResultSet res = statement.executeQuery()) {
            List<Map> rideList = new ArrayList<>();
            while (res.next()) {
                Map ride = new Map();
                ride.put("time_start", res.getString(1));
                ride.put("places_count", res.getInt(2));
                ride.put("creator_id", res.getString(3));
                ride.put("ride_id", res.getString(4));
                ride.put("lat_start", res.getDouble(5));
                ride.put("lon_start", res.getDouble(6));
                ride.put("lat_end", res.getDouble(7));
                ride.put("lon_end", res.getDouble(8));
                rideList.add(ride);
            }
            return Map.oneValue("rides", rideList);
        }
    }
}
