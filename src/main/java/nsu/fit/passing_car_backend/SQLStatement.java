package nsu.fit.passing_car_backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQLStatement {
    abstract protected HashMap<String, Class<?>> getAssert();

    abstract protected String getSQL();

    abstract protected Map run(PreparedStatement statement, Map data) throws SQLException;

    private void goAssert(Map data) throws DataError {
        HashMap<String, Class<?>> types = getAssert();
        for (String key : data.keySet()) {
            if (!types.containsKey(key)) {
                throw new DataError(DataError.UNKNOWN_FIELD, key);
            }
            try {
                types.get(key).cast(data.get(key));
            } catch (ClassCastException e) {
                throw new DataError(DataError.WRONG_TYPE_FIELD, key);
            }
        }
        for (String key : types.keySet()) {
            if (!data.containsKey(key)) {
                throw new DataError(DataError.MISSED_FIELD, key);
            }
        }
    }

    public Map go(Connection connection, Map data) throws DataError {
        goAssert(data);
        try (PreparedStatement statement = connection.prepareStatement(getSQL())) {
            return run(statement, data);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataError(DataError.UNKNOWN_ERROR, "SQLException");
        }
    }

    public static class Map extends HashMap<String, Object> {
        public static Map oneResult(Object result) {
            Map map = new Map();
            map.put("result", result);
            return map;
        }
    }

    public static class AssertMap extends HashMap<String, Class<?>> {

    }

}
