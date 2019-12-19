package nsu.fit.passing_car_backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
            if (is == null) {
                throw new IOException("No JSON and any data in connection", e);
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        is.transferTo(os);
        is.close();
        return (JSONObject) new JSONParser().parse(os.toString("UTF-8"));
    }

    public static void putJSON(HttpURLConnection con, JSONObject o) throws IOException {
        OutputStream os = con.getOutputStream();
        os.write(o.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
    }
}
