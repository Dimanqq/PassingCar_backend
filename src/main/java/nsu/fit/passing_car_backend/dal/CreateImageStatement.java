package nsu.fit.passing_car_backend.dal;

import nsu.fit.passing_car_backend.SQLStatement;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateImageStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap val = new AssertMap();
        val.put("stream", InputStream.class);
        val.put("mimeType", String.class);
        return val;
    }

    @Override
    protected String getSQL() {
        return "INSERT INTO image (mime_type, data) VALUES (?, ?) RETURNING id";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("mimeType"));
        statement.setBinaryStream(2, (InputStream) data.get("stream"));
        try (ResultSet res = statement.executeQuery()) {
            res.next();
            return Map.oneValue("image_id", res.getString(1));
        }
    }
}
