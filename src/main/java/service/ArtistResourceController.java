package service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.theads.CallableRequest;
import service.util.SSLDisabler;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ArtistResourceController {
    long timeout = 5000;
    static {
        SSLDisabler.disableSslVerification();
    }

    @RequestMapping(value = "/artist", method = GET, params = "id")
    public Callable<ResponseEntity<?>> artistInfo(@RequestParam(value="id", defaultValue="0") String id) {
        CallableRequest request = new CallableRequest(id);
        request.start();
        return request;
    }


}