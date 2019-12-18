package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class GetRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() {
        URL url;
        try {
            String user_id = new RegistrationTest().registrateUser();
            String ride_id = new CreateRideTest().createRide(user_id);
            url = new URL("http://localhost:8080/rides/" + ride_id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", user_id);
            assertEquals(200, con.getResponseCode());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
