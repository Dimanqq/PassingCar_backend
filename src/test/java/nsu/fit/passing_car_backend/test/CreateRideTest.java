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
    public void test() {
        URL url;
        try {
            url = new URL("http://localhost:8080/create/ride");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "2ca8e4e6-218e-11ea-b13d-0242ac110002");
            JSONObject o = new JSONObject();
            o.put("lat_start", "4332224.43");
            o.put("lat_end", "400224.43");
            o.put("lon_start", "3298922.29");
            o.put("lon_end", "3200922.29");
            o.put("time_start", "22:19");
            o.put("places_count", "3");
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println(br.readLine());
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(br);
            assertNotNull(res.get("ride_id"));
            assertEquals(201, con.getResponseCode());
            System.out.println(res.get("ride_id"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
