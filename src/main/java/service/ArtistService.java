package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class ArtistService implements AsyncConfigurer {

    /**
     * Here we configure thread pool executor.
     *
     * @return executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10000);
        executor.setKeepAliveSeconds(90);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix("ArtistService-");
        executor.initialize();
        return executor;
    }

    public static void main(String[] args){
        SpringApplication.run(ArtistService.class, args);
    }


}
