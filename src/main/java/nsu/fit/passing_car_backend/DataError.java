package nsu.fit.passing_car_backend;

import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;

public class DataError extends Exception {
    public static int UNKNOWN_FIELD = 1;
    public static int WRONG_TYPE_FIELD = 2;
    public static int MISSED_FIELD = 3;
    public static int UNKNOWN_ERROR = 4;
    public static int ALREADY_INVITE = 5;
    public static int NO_FREE_PLACES = 6;
    public static int UNAUTHORIZED = 7;
    public int code;
    public String desc;

    public DataError(int code, String desc) {
        super(desc + " [" + code + "]");
        this.code = code;
        this.desc = desc;
    }

    public void send(HttpServerExchange exchange) {
        JSONObject er = new JSONObject();
        er.put("error_code", code);
        er.put("error_desc", desc);
        exchange.setStatusCode(400);
        exchange.getResponseSender().send(er.toString());
    }
}
