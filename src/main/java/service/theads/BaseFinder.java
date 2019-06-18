package service.theads;

import service.Constants;
import service.json.JsonConverter;
import service.util.ConsoleLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BaseFinder extends Thread {

    private  String requestUrl;
    JsonConverter converter = new JsonConverter();
    private long startTime;
    private long endTime;
    Semaphore semaphore;

    public BaseFinder(String url,Semaphore semaphore){
        this.requestUrl=url;
        this.semaphore=semaphore;
    }
    @Override
    public void run() {
        semaphore.increment();
        startTimeRecord();
        try {
            URL url = new URL(requestUrl);
            int statusCode = connect(url);
            ConsoleLogger.log("Response received with code " + statusCode);
            if (statusCode == 200) {
              execute(url);
            }
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
        }
        long ms = endTimeRecord();
        ConsoleLogger.log(this.getClass().getName()+" executed request in "+ms+" ms");
        semaphore.decrement();
    }

    protected String execute(URL url) throws IOException {
        return readResponse(url);
    }

    private int connect(URL url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        return http.getResponseCode();
    }

    private String readResponse(URL url) throws IOException {
        String response = Constants.EMPTY_STING;
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            response += scanner.nextLine();
        }
        scanner.close();
        return response;
    }
    private long endTimeRecord() {
        endTime = System.currentTimeMillis();
        return endTime-startTime;
    }

    private void startTimeRecord() {
        startTime = System.currentTimeMillis();
    }
}
