package service.executors;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphore is shared between concurrent executors and counts amount of live executors
 */
public class Semaphore {
    //shared atomic integer
    private volatile AtomicInteger counter=new AtomicInteger(0);

    //atomic read
    int getValue() {
        return counter.get();
    }

    //atomic read and increment
    void increment() {
        counter.getAndIncrement();
    }

    //atomic read decrement
    void decrement() {
        counter.getAndDecrement();
    }
}
