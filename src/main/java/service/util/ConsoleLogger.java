package service.util;

/**
 * Logs info and errors to console if active.
 * To activate run application with option -log
 */
public class ConsoleLogger {

    private static boolean isActive = false;

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

}
