package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class GetUserTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        URL url;
        String user_id = new RegistrationTest().registrateUser();
        url = new URL("http://localhost:8080/users/" + user_id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", user_id);
        assertEquals(200, con.getResponseCode());
        con.disconnect();
    }
}
