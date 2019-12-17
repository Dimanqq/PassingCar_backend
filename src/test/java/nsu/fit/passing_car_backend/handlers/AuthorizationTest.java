package nsu.fit.passing_car_backend.handlers;

import nsu.fit.passing_car_backend.Initializer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class AuthorizationTest {

    @BeforeClass
    static public void init() {
        Initializer.init();
    }

    @Test
    public void testOne() throws IOException {
        System.out.println("HI");
        URLConnection c = new URL("http://localhost:8080/res").openConnection();
        c.setDoOutput(true);
        c.connect();
        c.getInputStream();
    }

    @Test
    public void testTwo() throws IOException {
        System.out.println("HI2");
        URLConnection c = new URL("http://localhost:8080/res").openConnection();
        c.setDoOutput(true);
        c.connect();
        c.getInputStream();
    }

}