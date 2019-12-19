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

public class GetRidesMemberTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        URL url;
        String memberId = new RegistrationTest().registrateUser();
        String id1, id2, id3;
        id1 = new InviteRideTest().inviteUser(memberId);
        //id2 = new InviteRideTest().inviteUser(memberId);
        //id3 = new InviteRideTest().inviteUser(memberId);
        url = new URL("http://localhost:8080/rides/member/" + memberId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", memberId);
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
            assertEquals(id1, o.get("ride_id"));
        }
    }
}
