package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRideStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("ride_id", String.class);
        map.put("user_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT" +
                "ride.time_start," +
                "ride.places_count," +
                "ride.creator_id," +
                "point_start.lat," +
                "point_start.lon," +
                "point_end.lat," +
                "point_end.lon" +
                "FROM ride" +
                "WHERE ride.id = ?::uuid" +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_start ON point_start.id = ride.point_start" +
                "JOIN (SELECT point.lat, point.lon, point.id FROM point) AS point_end ON point_end.id = ride.point_end";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("ride_id"));
        ResultSet res = statement.executeQuery();
        if(!res.next()){
            throw new DataError(DataError.NOT_FOUND, "Ride not found");
        }
        Map info = new Map();
        info.put("time_start", res.getString(1));
        info.put("places_count", res.getInt(2));
        info.put("creator_id", res.getInt(3));
        info.put("lat_start", res.getDouble(4));
        info.put("lon_start", res.getDouble(5));
        info.put("lat_end", res.getDouble(6));
        info.put("lon_end", res.getDouble(7));
        return info;
    }
}
