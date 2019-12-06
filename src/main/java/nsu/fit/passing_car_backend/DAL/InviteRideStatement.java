package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InviteRideStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("user_id", String.class);
        map.put("ride_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT\n" +
                "\t(\n" +
                "\t\tSELECT ride.places_count FROM ride\n" +
                "\t\tWHERE ride.id = glb.ride_id\n" +
                "\t) - (\n" +
                "\t\tSELECT COUNT(*) FROM m2m_ride_user\n" +
                "\t\tWHERE m2m_ride_user.ride_id = glb.ride_id\n" +
                "\t), (\n" +
                "\t\tSELECT COUNT(*) FROM m2m_ride_user\n" +
                "\t\tWHERE m2m_ride_user.ride_id = glb.ride_id\n" +
                "\t\tAND m2m_ride_user.user_id = glb.user_id\n" +
                "\t) > 0 FROM (\n" +
                "\t\tSELECT\n" +
                "\t\t\t?::uuid AS user_id,\n" +
                "\t\t\t?::uuid AS ride_id\n" +
                "\t) AS glb\n";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("user_id"));
        statement.setString(2, (String) data.get("ride_id"));
        ResultSet res = statement.executeQuery();
        res.next();
        Map info = new Map();
        info.put("free_places", res.getInt(1));
        info.put("already_invite", res.getBoolean(2));
        return info;
    }
}
