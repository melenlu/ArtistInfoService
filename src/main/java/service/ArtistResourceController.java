package service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.executors.CallableRequest;
import service.executors.RequestLimiter;
import service.executors.ScheduledTask;
import service.json.JsonConverter;
import service.json.model.external.Artist;

import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ArtistResourceController {
    private RequestLimiter requestLimiter = new RequestLimiter();

    /**
     * Artist Info request. Each request executed in separate thread.
     *
     * @param id - artist mbid
     * @return {@link Callable} thread with execution logic
     */
    @RequestMapping(value = "/artist", method = GET, params = "id")
    public Callable<ResponseEntity<?>> artistInfo(@RequestParam(value = "id", defaultValue = "0") String id) {
        return getResponseEntityCallable(id);
    }

    private Callable<ResponseEntity<?>> getResponseEntityCallable(@RequestParam(value = "id", defaultValue = "0") String id) {
        // ConsoleLogger.activate(); //uncomment if you want to log
        Artist artist = ArtistsCache.getInstance().getArtist(id);
        if (artist != null) {
            JsonConverter converter = new JsonConverter();
            return () -> ResponseEntity.ok(converter.toJson(artist));
        } else {
            CallableRequest request = new CallableRequest(id);
            ScheduledTask scheduledTask = new ScheduledTask(request);
            requestLimiter.enqueueRequest(scheduledTask);
            return scheduledTask;
        }
    }



}