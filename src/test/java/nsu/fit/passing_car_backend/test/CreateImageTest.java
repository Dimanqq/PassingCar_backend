package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Helpers;
import nsu.fit.passing_car_backend.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;

public class CreateImageTest {

    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException, ParseException {
        createImage(new RegistrationTest().registrateUser());
    }

    private void createImage(String user_id) throws IOException, ParseException {
        byte[] img = new byte[10000];
        for (int i = 0; i < img.length; i++) {
            img[i] = (byte) Math.round(Math.random() * 256);
        }
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/create/image")
                .openConnection();
        con.setRequestProperty("Authorization", user_id);
        con.setRequestProperty("Content-Type", "image/test");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        OutputStream os = con.getOutputStream();
        os.write(img);
        os.flush();
        JSONObject o = Helpers.getJSON(con);
        String image_id = (String) o.get("image_id");
        con.disconnect();
        byte[] imgr = getImage(image_id);
        assertEquals(img.length, imgr.length);
        for (int i = 0; i < img.length; i++) {
            assertEquals(img[i], imgr[i]);
        }
    }

    private byte[] getImage(String image_id) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/images/" + image_id)
                .openConnection();
        con.setDoInput(true);
        con.setRequestMethod("GET");
        InputStream is = con.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        is.transferTo(os);
        is.close();
        con.disconnect();
        return os.toByteArray();
    }
}
