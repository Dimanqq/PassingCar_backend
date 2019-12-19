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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RegistrationTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        registrateUser();
    }

    public String registrateUser() throws IOException, ParseException {
        URL url;
        url = new URL("http://localhost:8080/create/user");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        JSONObject o = new JSONObject();
        o.put("first_name", "Aya" + Math.random());
        o.put("last_name", "Matveeva" + Math.random());
        o.put("phone", "77999777" + Math.random());
        o.put("password", "1111111" + Math.random());
        o.put("email", "aya@yandex.ru" + Math.random());
        OutputStream out = con.getOutputStream();
        out.write(o.toString().getBytes(StandardCharsets.UTF_8));
        InputStream stream = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        JSONParser p = new JSONParser();
        JSONObject res = (JSONObject) p.parse(br);
        assertNotNull(res.get("user_id"));
        assertEquals(201, con.getResponseCode());
        stream.close();
        con.disconnect();
        return (String) res.get("user_id");
    }
}