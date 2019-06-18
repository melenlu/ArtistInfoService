package service.theads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import service.ArtistInfoReceiver;
import service.util.ConsoleLogger;

import java.util.concurrent.Callable;

public class CallableRequest extends Thread implements Callable<ResponseEntity<?>>{
    Semaphore semaphore = new Semaphore();

    ArtistInfoReceiver receiver = ArtistInfoReceiver.getNewInstance(semaphore);
    String id;
    public CallableRequest(String id){
        this.id=id;
    }

    @Override
    public ResponseEntity<?> call() {
        try {
            callGetArtistInfo();
            while (semaphore.getValue() > 0) {
               sleep(100);
            }
            return ResponseEntity.ok(refreshedResponse());
        }
        catch (Exception ex){
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
