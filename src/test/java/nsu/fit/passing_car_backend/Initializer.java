package nsu.fit.passing_car_backend;

public class Initializer {
    private static boolean isInit = false;
    public static synchronized void init(){
        if(!isInit){
            isInit = true;
            Main.main(new String[]{});
        }
    }
}
