package nsu.fit.passing_car_backend;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQLStatement {
    private Connection connection;
    abstract protected AssertMap getAssert();

    abstract protected String getSQL();

    abstract protected Map run(PreparedStatement statement, Map data) throws SQLException;

    private void goAssert(Map data) throws DataError {
        AssertMap types = getAssert();
        if (types == null) {
            return;
        }
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
            if (!data.containsKey(key) || data.get(key) == null) {
                throw new DataError(DataError.MISSED_FIELD, key);
            }
        }
    }

    public Map go(Connection connection, Map data) throws DataError {
        this.connection = connection;
        goAssert(data);
        try (PreparedStatement statement = connection.prepareStatement(getSQL())) {
            return run(statement, data);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataError(DataError.UNKNOWN_ERROR, "SQLException");
        }
    }

    public static class Map extends HashMap<String, Object> {
        public static Map oneValue(String key, Object result) {
            Map map = new Map();
            map.put(key, result);
            return map;
        }

        public static Map oneValue(Object result) {
            return oneValue("value", result);
        }

        private static void copy(java.util.Map a, java.util.Map b){
            for(Object key: a.keySet()){
                b.put(key, a.get(key));
            }
        }

        public static Map fromJSON(JSONObject data){
            Map map = new Map();
            copy(data, map);
            return map;
        }

        public Object value() {
            return get("value");
        }

        public JSONObject toJSON(){
            JSONObject o = new JSONObject();
            copy(this, o);
            return o;
        }
    }

    public static class AssertMap extends HashMap<String, Class<?>> {

    }

}
