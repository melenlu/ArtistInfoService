package service.threads;

import org.springframework.http.ResponseEntity;
import service.ArtistInfoReceiver;
import service.util.ConsoleLogger;

import java.util.concurrent.Callable;

/**
 * Callable thread to process REST resource request.
 * Creates and monitors {@link Semaphore} value which is
 * shared between subordinate threads.
 * Has execution timeout 15 sec by default
 */
public class CallableRequest extends Thread implements Callable<ResponseEntity<?>>{
    private Semaphore semaphore = new Semaphore();
    private ArtistInfoReceiver receiver = ArtistInfoReceiver.getNewInstance(semaphore);
    private long timeout = 15000;
    private String id;

    public CallableRequest(String id){
        this.id=id;
    }

    /**
     * Starts request execution. Multiple threads are executing logic in parallel
     * and fill requested object. They increment semaphore value on start of execution
     * and decrement on finish. So we expect zero value of semaphore when all threads
     * finished their work.
     *
     * @return {@link service.json.model.external.Artist} entity in JSON format wrapped
     * into {@link ResponseEntity}.
     */
    @Override
    public ResponseEntity<?> call() {
        try {
            //initiate logic execution
            callGetArtistInfo();
            //we are waiting of all threads work completion
            //or timeout
            while (semaphore.getValue() > 0 && timeout > 0) {
                sleep(100);
                timeout -= 100;
            }
            //exited by timeout
            if (timeout <= 0) {
                return ResponseEntity.badRequest().body("Sorry. Service is very busy now. Try to repeat your request later.");
            }
            //refresh artist value by taking fully
            //prefilled object from cache
            return ResponseEntity.ok(refreshedResponse());
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
            return ResponseEntity.badRequest().body("Something went wrong. Don't worry and try again.");
        }
    }

    private void callGetArtistInfo() {
        receiver.getArtistInfoInJSON(id);
    }

    private String refreshedResponse(){
        return receiver.getArtistInfoInJSON(id);
    }
}
