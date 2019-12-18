package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class GetRidesCreatorTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        URL url;
        String creatorId = new RegistrationTest().registrateUser();
        new CreateRideTest().createRide(creatorId);
        new CreateRideTest().createRide(creatorId);
        new CreateRideTest().createRide(creatorId);
        new CreateRideTest().createRide(creatorId);
        url = new URL("http://localhost:8080/rides/creator/" + creatorId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", creatorId);
        InputStream stream;
        try {
            stream = con.getInputStream();
        } catch (IOException e) {
            stream = con.getErrorStream();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        assertEquals(200, con.getResponseCode());
        JSONParser parser = new JSONParser();
        JSONObject res = (JSONObject) parser.parse(br);
        JSONArray list = (JSONArray) res.get("rides");
        for (Object it : list
        ) {
            JSONObject o = (JSONObject) it;
            assertEquals(creatorId, o.get("creator_id"));
        }
    }

}
