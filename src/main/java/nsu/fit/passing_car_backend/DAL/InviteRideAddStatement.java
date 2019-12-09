package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InviteRideAddStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        return null;
    }

    @Override
    protected String getSQL() {
        return "INSERT INTO \"m2m_ride_user\" (user_id, ride_id) VALUES (?::uuid, ?::uuid)";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("user_id"));
        statement.setString(2, (String) data.get("ride_id"));
        statement.execute();
        return null;
    }
}
