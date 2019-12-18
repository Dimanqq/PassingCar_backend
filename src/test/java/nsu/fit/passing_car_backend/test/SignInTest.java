package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

public class SignInTest {
    @BeforeClass
    static public void init() {
        Initializer.init();
    }

    @Test
    public void handleRequest() {
        URL url;
        try {
            url = new URL("http://localhost:8080/signin");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            JSONObject o = new JSONObject();
            o.put("password", "1111111");
            o.put("email", "alina@yandex.ru");
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(br);
            assertNotNull(res.get("user_id"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}