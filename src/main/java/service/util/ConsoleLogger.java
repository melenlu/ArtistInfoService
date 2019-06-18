package service.util;

public class ConsoleLogger {
    private volatile static boolean isActive =true;
    public static void log(String message){
        if(isActive){
            System.out.println(message);
        }
    }
    public static void error(Exception ex){
        if(isActive){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void activate(){
        isActive =true;
    }
    public static void deactivate(){
        isActive =false;
    }
}
