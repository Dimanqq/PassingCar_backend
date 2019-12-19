package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class GetRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        String user_id = new RegistrationTest().registrateUser();
        String ride_id = new CreateRideTest().createRide(user_id);
        getRide(user_id, ride_id);
    }

    public JSONObject getRide(String user_id, String ride_id) throws IOException, ParseException {
        URL url;

        url = new URL("http://localhost:8080/rides/" + ride_id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", user_id);
        assertEquals(200, con.getResponseCode());
        InputStream i = con.getInputStream();
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        i.transferTo(o);
        i.close();
        return (JSONObject) new JSONParser().parse(o.toString("UTF-8"));
    }
}
