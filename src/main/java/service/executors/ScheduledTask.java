package service.executors;

import service.util.ConsoleLogger;

import java.util.TimerTask;
import java.util.concurrent.Callable;

public class ScheduledTask extends TimerTask implements Callable {
    private CallableRequest callable;

    public ScheduledTask(CallableRequest callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        ConsoleLogger.log(this.toString() + " Schedulled task started " + System.currentTimeMillis());
        callable.start();
    }

    @Override
    public Object call() throws Exception {
        while (this.scheduledExecutionTime() > System.currentTimeMillis()) {
            Thread.sleep(100);
        }
        ConsoleLogger.log(this.toString() + "Schedulled task returns value. " + System.currentTimeMillis());
        return callable.call();

    }
}
