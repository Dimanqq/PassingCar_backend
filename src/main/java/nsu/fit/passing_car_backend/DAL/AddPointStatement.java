package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddPointStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("lat", Double.class);
        map.put("lon", Double.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "INSERT INTO \"point\" (lat, lon) VALUES (?, ?) RETURNING id";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setDouble(1, (Double) data.get("lat"));
        statement.setDouble(2, (Double) data.get("lon"));
        try (ResultSet res = statement.executeQuery()) {
            res.next();
            return Map.oneValue(res.getString(1));
        }
    }
}
