package service.threads;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphore is shared between concurrent threads and counts amount of live threads
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
