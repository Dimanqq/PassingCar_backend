package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRidesMemberStatement extends SQLStatement {
    @Override
    protected SQLStatement.AssertMap getAssert() {
        SQLStatement.AssertMap map = new SQLStatement.AssertMap();
        map.put("user_id", String.class);//todo
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT " +
                "\"ride\".id" +
                "\"ride\".point_start" +
                "\"ride\".point_end" +
                "\"ride\".time_start" +
                "\"ride\".places_count" +
                "\"ride\".creator_id" +
                "FROM \"ride\" WHERE \"ride\".creator_id = ?::uuid";
    }

    @Override
    protected SQLStatement.Map run(PreparedStatement statement, SQLStatement.Map data) throws SQLException, DataError {
        statement.setString(1, (String) data.get("user_id"));
        SQLStatement.Map user;
        try (ResultSet res = statement.executeQuery()) {
            if (!res.next()) {
                throw new DataError(DataError.NOT_FOUND, "User not found");
            }
            user = new SQLStatement.Map();
            user.put("email", res.getString(1));
            user.put("first_name", res.getString(2));
            user.put("last_name", res.getString(3));
            user.put("phone", res.getString(4));
        }
        return user;
    }
}
