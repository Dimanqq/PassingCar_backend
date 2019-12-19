package nsu.fit.passing_car_backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Helpers {
    private static String enc(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    public static String makeQuery(JSONObject o) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object key : o.keySet()) {
            sb
                    .append(first ? "?" : "&")
                    .append(enc((String) key))
                    .append("=")
                    .append(enc(String.valueOf(o.get(key))));
            first = false;
        }
        return sb.toString();
    }

    public static JSONObject getJSON(HttpURLConnection con) throws IOException, ParseException {
        InputStream is;
        try {
            is = con.getInputStream();
        } catch (IOException e) {
            is = con.getErrorStream();
        }
        if (is == null) {
            System.out.println(con);
            throw new IOException("sdf");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        is.transferTo(os);
        return (JSONObject) new JSONParser().parse(os.toString("UTF-8"));
    }
}
