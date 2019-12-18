package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class InviteRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        URL url;
        String ride_id, user_id;
        user_id = new RegistrationTest().registrateUser();
        ride_id = new CreateRideTest().createRide(user_id);
        url = new URL("http://localhost:8080/rides/" + ride_id + "/invite");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", user_id);
        assertEquals(201, con.getResponseCode());
    }
}