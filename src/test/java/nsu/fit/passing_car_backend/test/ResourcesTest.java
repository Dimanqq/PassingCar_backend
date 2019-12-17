package nsu.fit.passing_car_backend.test;

import nsu.fit.passing_car_backend.Initializer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class ResourcesTest {
    @BeforeClass
    public static void init() {
        Initializer.init();
    }

    @Test
    public void test() throws IOException {
        URLConnection c = new URL("http://localhost:8080/res").openConnection();
        c.setDoOutput(true);
        c.connect();
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        InputStream i = c.getInputStream();
        i.transferTo(o);
        i.close();
        assert o.toString(String.valueOf(Charset.defaultCharset())).contains("<!-- For tests -->");
    }
}
