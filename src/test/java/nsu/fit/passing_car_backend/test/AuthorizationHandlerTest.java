package nsu.fit.passing_car_backend.test;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthorizationHandlerTest {

    @Test
    public void handleRequest() {
        /*URL url;
        try {
            url = new URL("http://localhost:8080/create/user");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            JSONObject o = new JSONObject();
            o.put("first_name", "Vlasov");
            o.put("last_name", "Ivan");
            o.put("password", "12345");
            o.put("phone", "999999999");
            o.put("email", "email@yandex.ru");
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            con.addRequestProperty("first_name", "Vlasov");
            con.addRequestProperty("last_name", "Ivan");
            con.addRequestProperty("password", "12345");
            con.addRequestProperty("phone", "999999999");
            con.addRequestProperty("email", "email@yandex.ru");

            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println(con.getRequestProperty("user_id"));
            System.out.println(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}