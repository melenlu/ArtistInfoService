package service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.threads.CallableRequest;
import service.util.SSLDisabler;

import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ArtistResourceController {
    static {
        SSLDisabler.disableSslVerification();
    }

    /**
     * Artist Info request. Each request executed in separate thread.
     *
     * @param id - artist mbid
     * @return {@link Callable} thread with execution logic
     */
    @RequestMapping(value = "/artist", method = GET, params = "id")
    public Callable<ResponseEntity<?>> artistInfo(@RequestParam(value="id", defaultValue="0") String id) {
        CallableRequest request = new CallableRequest(id);
        request.start();
        return request;
    }


}