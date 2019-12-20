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
import static org.junit.Assert.assertTrue;

public class GetMembersRideTest {

    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        String creatorId = new RegistrationTest().registrateUser();
        String rideId = new CreateRideTest().createRide(creatorId);
        String id1, id2, id3;
        id1 = new InviteRideTest().inviteRide(rideId);
        id2 = new InviteRideTest().inviteRide(rideId);
        id3 = new InviteRideTest().inviteRide(rideId);

        JSONArray list = getMembers(rideId, creatorId);
        for (Object it : list) {
            JSONObject o = (JSONObject) it;
            assertTrue(o.get("id").equals(id1)
                    || o.get("id").equals(id2)
                    || o.get("id").equals(id3)
                    || o.get("id").equals(creatorId));
        }

    }

    public JSONArray getMembers(String rideId, String creatorId) throws IOException, ParseException {
        URL url;
        url = new URL("http://localhost:8080/users/member/" + rideId);
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
        JSONArray list = (JSONArray) res.get("users");
        stream.close();
        con.disconnect();
        return list;
    }
}
