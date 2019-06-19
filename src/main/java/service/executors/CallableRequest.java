package service.executors;

import org.springframework.http.ResponseEntity;
import service.ArtistInfoReceiver;
import service.Constants;
import service.json.JsonConverter;
import service.json.model.external.Artist;
import service.util.ConsoleLogger;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Callable thread to process REST resource request.
 * Creates and monitors {@link Semaphore} value which is
 * shared between subordinate executors.
 * Has execution timeout 15 sec by default
 */
public class CallableRequest extends Thread implements Callable<ResponseEntity<?>>{
    private static final int TIMEOUT_STEP = 100;
    private Semaphore semaphore = new Semaphore();
    private ArtistInfoReceiver receiver = ArtistInfoReceiver.getNewInstance(semaphore);
    private long timeout = 60 * 1000 * 2;
    private String id;

    public CallableRequest(String id){
        this.id=id;
    }

    /**
     * Starts request execution. Multiple executors are executing logic in parallel
     * and fill requested object. They increment semaphore value on start of execution
     * and decrement on finish. So we expect zero value of semaphore when all executors
     * finished their work.
     *
     * @return {@link service.json.model.external.Artist} entity in JSON format wrapped
     * into {@link ResponseEntity}.
     */
    @Override
    public ResponseEntity<?> call() {
        try {
            //initiate logic execution
            Artist artist = callGetArtistInfo();

            //if artist not found return bad request
            if (Objects.isNull(artist)) {
                return ResponseEntity.badRequest().body(Constants.ARTIST_NOT_FOUND);
            }
            //we are waiting of all executors work completion
            //or timeout
            while (semaphore.getValue() > 0 && timeout > 0) {
                sleep(TIMEOUT_STEP);
                timeout -= TIMEOUT_STEP;
            }
            //exited by timeout
            if (timeout <= 0) {
                ConsoleLogger.log(Constants.TIMEOUT);
                return ResponseEntity.badRequest().body(Constants.REPEAT_YOUR_REQUEST_LATER);
            }

            JsonConverter converter = new JsonConverter();
            //refresh artist value by taking fully
            //prefilled object from cache
            return ResponseEntity.ok(converter.toJson(artist));
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
            return ResponseEntity.badRequest().body(Constants.BAD_REQUEST);
        }
    }

    private Artist callGetArtistInfo() {
        return receiver.getArtistInfo(id);
    }

    @Override
    public void run() {

    }
}
