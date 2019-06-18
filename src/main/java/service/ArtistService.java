package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import service.util.ConsoleLogger;

@SpringBootApplication
@EnableAsync
public class ArtistService {
    public static void main(String[] args){
        applyOptions(args);
        SpringApplication.run(ArtistService.class, args);
    }

    private static void applyOptions(String[] args) {
        if(args!=null && args.length>0){
            for (String arg:args) {
                if(arg.equals("-log")){
                    ConsoleLogger.activate();
                }
            }
        }
    }

}
