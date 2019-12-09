package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.DataError;
import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetRideStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        return null;
    }

    @Override
    protected String getSQL() {
        return null;
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError {
        return null;
    }
}
