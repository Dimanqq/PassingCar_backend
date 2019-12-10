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
    public void handleRequest() {
        URL url;
        try {
            url = new URL("http://localhost:8080/create/user");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            JSONObject o = new JSONObject();
            o.put("first_name", "Vlasov");
            o.put("last_name", "Ivan");
            o.put("password", "12345");
            o.put("phone", new Random().nextInt());
            o.put("email", new Random().nextInt() + "@yandex.ru");
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println(br.readLine());
            JSONParser p = new JSONParser();
            JSONObject res = (JSONObject) p.parse(br);
            assertNotNull(res.get("user_id"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}