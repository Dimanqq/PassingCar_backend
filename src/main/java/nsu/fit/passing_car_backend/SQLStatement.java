package nsu.fit.passing_car_backend;

import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQLStatement {

    abstract protected AssertMap getAssert();

    abstract protected String getSQL();

    abstract protected Map run(PreparedStatement statement, Map data) throws SQLException, DataError;

    private void goAssert(Map data) throws DataError {
        AssertMap types = getAssert();
        if (types == null) {
            return;
        }
        for (Map.Entry<String,Object> entry : data.entrySet()) {
            if (!types.containsKey(entry.getKey())) {
                throw new DataError(DataError.UNKNOWN_FIELD, entry.getKey());
            }
            try {
                types.get(entry.getKey()).cast(data.get(entry.getKey()));
            } catch (ClassCastException e) {
                throw new DataError(DataError.WRONG_TYPE_FIELD, entry.getKey());
            }
        }
        for (Map.Entry<String,Object> entry : data.entrySet()) {
            if (!data.containsKey(entry.getKey()) || data.get(entry.getKey()) == null) {
                throw new DataError(DataError.MISSED_FIELD, entry.getKey());
            }
        }
    }

    public Map go(Connection connection, Map data) throws DataError {
        goAssert(data);
        try (PreparedStatement statement = connection.prepareStatement(getSQL())) {
            return run(statement, data);
        } catch (PSQLException e) {
            if (e.getServerErrorMessage() != null) {
                String constraint = e.getServerErrorMessage().getConstraint();
                if (constraint != null) {
                    throw new DataError(DataError.DUPLICATE, constraint);
                }
            }
            e.printStackTrace();
            System.err.println(getSQL());
            throw new DataError(DataError.UNKNOWN_ERROR, "PSQLException");
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

        private static void copy(java.util.Map a, java.util.Map b) {
            for (Object key : a.keySet()) {
                b.put(key, a.get(key));
            }
        }

        public static Map fromJSON(JSONObject data) {
            Map map = new Map();
            copy(data, map);
            return map;
        }

        public static Map fromExchangeJSON(HttpServerExchange exchange) throws DataError {
            Reader reader = new BufferedReader(new InputStreamReader(
                    exchange.getInputStream(),
                    StandardCharsets.UTF_8
            ));
            try {
                return Map.fromJSON((JSONObject) new JSONParser().parse(reader));
            } catch (IOException e) {
                e.printStackTrace();
                throw new DataError(DataError.UNKNOWN_ERROR, "JSON read");
            } catch (ParseException e) {
                throw new DataError(DataError.MISSED_FIELD, "Wrong JSON");
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static Map fromExchangeQuery(HttpServerExchange exchange) {
            Map data = new Map();
            for (String key : exchange.getQueryParameters().keySet()) {
                String d = exchange.getQueryParameters().get(key).getFirst();
                try {
                    data.put(key, Double.valueOf(d));
                    continue;
                } catch (NumberFormatException ignored) {
                }
                try {
                    data.put(key, Integer.valueOf(d));
                    continue;
                } catch (NumberFormatException ignored){
                }
                data.put(key, d);
            }
            return data;
        }

        public Object value() {
            return get("value");
        }

        public JSONObject toJSON() {
            JSONObject o = new JSONObject();
            copy(this, o);
            return o;
        }
    }

    public static class AssertMap extends HashMap<String, Class<?>> {

    }

}
