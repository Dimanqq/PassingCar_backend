package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        createRide(new RegistrationTest().test());
    }

    public String createRide(String user_id) throws IOException, ParseException {
        URL url;
        url = new URL("http://localhost:8080/create/ride");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", user_id);
        JSONObject o = new JSONObject();
        o.put("lat_start", 4332224.43);
        o.put("lat_end", 400224.43);
        o.put("lon_start", 3298922.29);
        o.put("lon_end", 3200922.29);
        o.put("time_start", "2019-12-18T13:18:44.599Z");
        o.put("places_count", 3);
        OutputStream out = con.getOutputStream();
        out.write(o.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();
        InputStream stream;
        try {
            stream = con.getInputStream();
        } catch (IOException e) {
            stream = con.getErrorStream();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        JSONParser parser = new JSONParser();
        JSONObject res = (JSONObject) parser.parse(br);
        assertNotNull(res.get("ride_id"));
        assertEquals(201, con.getResponseCode());
        return (String) res.get("ride_id");
    }
}
