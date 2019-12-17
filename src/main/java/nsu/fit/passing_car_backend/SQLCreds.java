package nsu.fit.passing_car_backend;

import java.util.Map;
import java.util.logging.Logger;

public class SQLCreds {
    String ip;
    int port;
    String db;
    String user;
    String password;

    private SQLCreds() {
    }

    public static SQLCreds loadFromEnv() {
        SQLCreds creds = new SQLCreds();
        Map<String, String> env = System.getenv();
        creds.port = Integer.valueOf(env.getOrDefault("POSTGRES_PORT", "5433"));
        creds.ip = env.getOrDefault("POSTGRES_IP", "3.19.71.72");
        creds.db = env.getOrDefault("POSTGRES_DATABASE", "passing_car");
        creds.user = env.getOrDefault("POSTGRES_USER", "postgres");
        creds.password = env.getOrDefault("POSTGRES_PASS", "qp~pq234");
        creds.outInfo();
        return creds;
    }

    private void outInfo() {
        Logger.getAnonymousLogger().info(
                "Connect to " + ip + ":" + port + "\n" +
                        "U[" + user + "] DB[" + db + "]"
        );
    }
}
