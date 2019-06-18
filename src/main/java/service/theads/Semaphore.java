package service.theads;

import java.util.concurrent.atomic.AtomicInteger;

public class Semaphore {
    private volatile AtomicInteger counter=new AtomicInteger(0);

    public int getValue(){
        return counter.get();
    }
    public void increment(){
        counter.getAndIncrement();
    }
    public void decrement(){
        counter.getAndDecrement();
    }
}
