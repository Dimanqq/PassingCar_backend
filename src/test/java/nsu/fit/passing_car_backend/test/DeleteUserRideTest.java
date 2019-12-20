package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class DeleteUserRideTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        URL url;
        String creatorId = new RegistrationTest().registrateUser();
        String rideId = new CreateRideTest().createRide(creatorId);
        String memberId = new InviteRideTest().inviteRide(rideId);
        url = new URL("http://localhost:8080/rides/" + rideId + "/" + memberId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", creatorId);
        InputStream stream;
        try {
            stream = con.getInputStream();
        } catch (IOException e) {
            stream = con.getErrorStream();
        }
        assertEquals(202, con.getResponseCode());
        JSONArray list = new GetMembersRideTest().getMembers(rideId, creatorId);
        assertEquals(1, list.size());
        stream.close();
        con.disconnect();
    }
}
