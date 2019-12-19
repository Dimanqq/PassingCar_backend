package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMembersRideStatement extends SQLStatement {
    @Override
    protected SQLStatement.AssertMap getAssert() {
        SQLStatement.AssertMap map = new SQLStatement.AssertMap();
        map.put("ride_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT " +
                "u.id::text, " +
                "u.email, " +
                "u.first_name, " +
                "u.last_name, " +
                "u.phone, " +
                "m2m_ride_user.ride_id::text " +
                "FROM m2m_ride_user " +
                "JOIN (SELECT " +
                "\"user\".id, " +
                "\"user\".email, " +
                "\"user\".first_name, " +
                "\"user\".last_name, " +
                "\"user\".phone " +
                "FROM \"user\") AS u " +
                "ON m2m_ride_user.user_id = u.id " +
                "WHERE m2m_ride_user.ride_id = ?::uuid";
    }

    @Override
    protected SQLStatement.Map run(PreparedStatement statement, SQLStatement.Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("ride_id"));
        try (ResultSet res = statement.executeQuery()) {
            List<SQLStatement.Map> userList = new ArrayList<>();
            while (res.next()) {
                SQLStatement.Map user = new SQLStatement.Map();
                user.put("id", res.getString(1));
                user.put("email", res.getString(2));
                user.put("first_name", res.getString(3));
                user.put("last_name", res.getString(4));
                user.put("phone", res.getString(5));
                userList.add(user);
            }
            return SQLStatement.Map.oneValue("users", userList);
        }
    }
}
