package nsu.fit.passing_car_backend;

import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;

public class DataError extends Exception {
    public static int DUPLICATE = 1;
    public int code;
    public String desc;
    public DataError(int code, String desc){
        super(desc + " [" + code + "]");
        this.code = code;
        this.desc = desc;
    }
    public void send(HttpServerExchange exchange){
        JSONObject er = new JSONObject();
        er.put("error_code", code);
        er.put("error_desc", desc);
        exchange.setStatusCode(400);
        exchange.getResponseSender().send(er.toString());
    }
}
