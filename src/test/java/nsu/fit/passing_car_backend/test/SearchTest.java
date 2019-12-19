package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        String user_id = new RegistrationTest().registrateUser();
        String ride_id = new CreateRideTest().createRide(user_id);
        JSONObject o1 = new GetRideTest().getRide(user_id, ride_id);
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/search?" +
                "time_start=" + o1.get("time_start") + "&" +
                "lat_start=" + o1.get("lat_start") + "&" +
                "lon_start=" + o1.get("lon_start") + "&" +
                "lat_end=" + o1.get("lat_end") + "&" +
                "lon_end=" + o1.get("lon_end") + "&" +
                "radius_start=3&" +
                "radius_end=3&" +
                "time_needed=" + o1.get("time_start") + "&" +
                "time_delta=1").openConnection();
        con.setRequestProperty("Authorization", user_id);
        con.setRequestMethod("GET");
        //con.setDoInput(true);
        //con.connect();
        InputStream i;
        //try {
        i = con.getInputStream();
        /*} catch (IOException e){
            //i = con.getErrorStream();
            //System.out.println(new String(i.readAllBytes()));
            return;
        }*/
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        i.transferTo(o);
        JSONObject o2 = (JSONObject) new JSONParser().parse(o.toString("UTF-8"));
        System.out.println(o1.toString());
        System.out.println(o2.toString());
    }
}
