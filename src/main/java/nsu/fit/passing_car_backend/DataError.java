package nsu.fit.passing_car_backend;

import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;

public class DataError extends Exception {
    public static final int MISSED_FIELD = 3;
    public static final int ALREADY_INVITE = 5;
    public static final int NO_FREE_PLACES = 6;
    public static final int UNAUTHORIZED = 7;
    public static final int NOT_FOUND = 8;
    static final int UNKNOWN_FIELD = 1;
    static final int WRONG_TYPE_FIELD = 2;
    static final int UNKNOWN_ERROR = 4;
    static final int DUPLICATE = 9;
    private final int code;
    private final String desc;

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
