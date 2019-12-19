package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Helpers;
import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        createRide(new RegistrationTest().registrateUser());
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
        o.put("places_count", 3L);
        Helpers.putJSON(con, o);
        JSONObject res = Helpers.getJSON(con);
        assertNotNull(res.get("ride_id"));
        assertEquals(201, con.getResponseCode());
        con.disconnect();
        String ride_id = (String) res.get("ride_id");
        JSONObject or = getRide(user_id, ride_id);
        assertEquals(o.get("lat_start"), or.get("lat_start"));
        assertEquals(o.get("lat_end"), or.get("lat_end"));
        assertEquals(o.get("lon_start"), or.get("lon_start"));
        assertEquals(o.get("lon_end"), or.get("lon_end"));
        assertEquals(o.get("places_count"), or.get("places_count"));
        //assertEquals(o.get("lat_start"), or.get("lat_start"));
        return ride_id;
    }

    public JSONObject getRide(String user_id, String ride_id) throws IOException, ParseException {
        URL url = new URL("http://localhost:8080/rides/" + ride_id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", user_id);
        assertEquals(200, con.getResponseCode());
        JSONObject o = Helpers.getJSON(con);
        con.disconnect();
        return o;
    }
}
