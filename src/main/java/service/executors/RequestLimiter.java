package service.executors;

import java.util.Timer;
import java.util.TimerTask;

public class RequestLimiter extends Timer {
    private long timeDelay = 3001; //average response time from musicbrainz
    private long multiplier = 0;
    private long lastStart;

    public void enqueueRequest(TimerTask thread) {
        //we increase delay only for concurrently running requests
        if (System.currentTimeMillis() - lastStart > timeDelay) {
            //if last request was long time ago we clear time delay multiplier
            multiplier = 0;
        }
        this.
                lastStart = System.currentTimeMillis();
        schedule(thread, timeDelay * multiplier++);
    }
}
