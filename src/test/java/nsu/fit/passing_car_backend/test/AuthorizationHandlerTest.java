package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthorizationHandlerTest {
    @BeforeClass
    static public void init(){
        Initializer.init();
    }

    @Test
    public void handleRequest() {
        URL url;
        try {
            url = new URL("http://localhost:8080/create/user");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            JSONObject o = new JSONObject();
            o.put("first_name", "Doytrrv");
            o.put("last_name", "Iaeewn");
            o.put("password", "14wew5");
            o.put("phone", "54346575");
            o.put("email", "r334522@yandex.ru");
            OutputStream out = con.getOutputStream();
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
            /*con.addRequestProperty("first_name", "Vlasov");
            con.addRequestProperty("last_name", "Ivan");
            con.addRequestProperty("password", "12345");
            con.addRequestProperty("phone", "999999999");
            con.addRequestProperty("email", "email@yandex.ru");*/

            InputStream stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            System.out.println(con.getRequestProperty("user_id"));
            System.out.println(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}