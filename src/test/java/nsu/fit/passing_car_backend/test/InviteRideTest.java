package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
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

public class InviteRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() {
        URL url;
        try {
            url = new URL("http://localhost:8080/rides/000/invite");//todo
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "b9555212-2193-11ea-9ee8-0242ac110002");
            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println(br.readLine());
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(br);
            assertEquals(201, con.getResponseCode()); // todo
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
