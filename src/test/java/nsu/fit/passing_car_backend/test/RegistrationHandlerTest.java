package nsu.fit.passing_car_backend.test;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RegistrationHandlerTest {

    @Test
    public void correctRequest() {
        URL url;
        try {
            url = new URL("http://localhost:8080/create/user");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            JSONObject o = new JSONObject();
            o.put("first_name", "Kirill");
            o.put("last_name", "Matveev");
            o.put("password", "1111111");
            o.put("phone", String.valueOf(new Random().nextInt()));
            o.put("email", String.valueOf(new Random().nextInt()));
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            JSONParser p = new JSONParser();
            JSONObject res = (JSONObject) p.parse(br);
            assertNotNull(res.get("user_id"));
            assertEquals(201, con.getResponseCode());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}