package service.threads;

import service.Constants;
import service.json.JsonConverter;
import service.util.ConsoleLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Abstract Finder class represents
 * logic executed in separate thread.
 * Each instance of any Finder class works with shared semaphore,
 * which is incremented in the beginning of logical transaction
 * and decremented in the end.
 */
public abstract class BaseLogicExecutor extends Thread {

    private  String requestUrl;
    private long startTime;
    private long endTime;
    private static int CODE_OK = 200;

    protected JsonConverter converter = new JsonConverter();
    private Semaphore semaphore;

    /**
     * Constructor accepts two parameters
     *
     * @param url       - url for external request
     * @param semaphore - shared thread counter
     */
    BaseLogicExecutor(String url, Semaphore semaphore) {
        this.requestUrl=url;
        this.semaphore=semaphore;
    }

    @Override
    public void run() {
        startTransaction();
        executeTransaction();
        endTransaction();
    }

    /**
     * Makes request to external resource and receives
     * data on {@code json} format if returned {@code code=200}
     */
    private void executeTransaction() {
        try {
            URL url = new URL(requestUrl);
            int statusCode = connect(url);
            ConsoleLogger.log("Response received with code " + statusCode);
            if (statusCode == CODE_OK) {
                execute(url);
            }
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
        }
    }

    private void startTransaction() {
        semaphore.increment();
        startTimeRecord();
    }

    private void endTransaction() {
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
        StringBuilder response = new StringBuilder(Constants.EMPTY_STING);
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response.toString();
    }

    private void startTimeRecord() {
        startTime = System.currentTimeMillis();
    }

    private long endTimeRecord() {
        endTime = System.currentTimeMillis();
        return endTime-startTime;
    }


}
