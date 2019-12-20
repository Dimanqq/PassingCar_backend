package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Helpers;
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
        String user_id = new RegistrationTest().registrateUser();
        inviteUser(user_id);
    }

    String inviteUser(String user_id) throws IOException, ParseException {
        String creator_id = new RegistrationTest().registrateUser();
        String ride_id = new CreateRideTest().createRide(creator_id);
        inviteFull(user_id, ride_id);
        return ride_id;
    }

    String inviteRide(String ride_id) throws IOException, ParseException {
        String member_id = new RegistrationTest().registrateUser();
        inviteFull(member_id, ride_id);
        return member_id;
    }

    private void inviteFull(String user_id, String ride_id) throws IOException, ParseException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/rides/" + ride_id + "/invite")
                .openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", user_id);
        //System.out.println(Helpers.getJSON(con));
        assertEquals(201, con.getResponseCode());
        con.disconnect();
    }
}
