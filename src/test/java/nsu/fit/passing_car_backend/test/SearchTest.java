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
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        String user_id = new RegistrationTest().registrateUser();
        String ride_id = new CreateRideTest().createRide(user_id);
        JSONObject o1 = new CreateRideTest().getRide(user_id, ride_id);
        JSONObject data = new JSONObject();
        data.put("lat_start", o1.get("lat_start"));
        data.put("lon_start", o1.get("lon_start"));
        data.put("lat_end", o1.get("lat_end"));
        data.put("lon_end", o1.get("lon_end"));
        data.put("radius_start", 3.0);
        data.put("radius_end", 3.0);
        data.put("time_needed", o1.get("time_start"));
        data.put("time_delta", 1.0);
        String url = "http://localhost:8080/search" + Helpers.makeQuery(data);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("Authorization", user_id);
        con.setRequestMethod("GET");
        JSONObject o2 = Helpers.getJSON(con);
        int cnt = 0;
        for (JSONObject ride : (List<JSONObject>) o2.get("rides")) {
            if (ride_id.equals(ride.get("ride_id"))) {
                cnt++;
            }
        }
        assertEquals(cnt, 1);
        // TODO: assert another fields
    }
}
