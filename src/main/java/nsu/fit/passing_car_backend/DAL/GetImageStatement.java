package nsu.fit.passing_car_backend.DAL;

import nsu.fit.passing_car_backend.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetImageStatement extends SQLStatement {
    @Override
    protected AssertMap getAssert() {
        AssertMap map = new AssertMap();
        map.put("image_id", String.class);
        return map;
    }

    @Override
    protected String getSQL() {
        return "SELECT image.mime_type, image.data FROM image WHERE image.id::text = ?";
    }

    @Override
    protected Map run(PreparedStatement statement, Map data) throws SQLException {
        statement.setString(1, (String) data.get("image_id"));
        Map map;
        try (ResultSet res = statement.executeQuery()) {
            res.next();
            map = new Map();
            map.put("mimeType", res.getString(1));
            map.put("stream", res.getBinaryStream(2));
        }
        return map;
    }
}
