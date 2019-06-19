package service;

import service.executors.AlbumCoverArtLogicExecutor;
import service.executors.PortfolioLogicExecutor;
import service.executors.Semaphore;
import service.json.JsonConverter;
import service.json.model.external.Album;
import service.json.model.external.Artist;
import service.json.model.external.Pair;
import service.util.ConsoleLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ArtistInfoReceiver {

    private JsonConverter jsonConverter = new JsonConverter();
    private Semaphore threadCounter;

    public ArtistInfoReceiver(Semaphore threadCounter) {
        this.threadCounter = threadCounter;
    }

    public static ArtistInfoReceiver getNewInstance(Semaphore threadCounter) {
        return new ArtistInfoReceiver(threadCounter);
    }

    public Artist getArtistInfo(String id) {
        ArtistsCache cache = ArtistsCache.getInstance();
        Artist cachedValue = cache.getArtist(id);
        if (cachedValue != null) {
            return cachedValue;
        } else {
            return receiveArtist(id);
        }
    }

    private Artist receiveArtist(String id) {
        String artistFullUrl = Constants.artistUrl + id + Constants.urlParams;//use string builder
        String artistResponse = requestArtistData(artistFullUrl);
        artistResponse = handleTooManyRequestsError(artistFullUrl, artistResponse);
        if (!artistResponse.equals(Constants.ERROR_503) && !artistResponse.equals(Constants.EMPTY_STING)) {
            Artist artist = prefillArtist(artistResponse);
            //caching received artist
            ArtistsCache.getInstance().putArtist(artist);
            //return json representation of the artist
            return artist;
        } else {
            ConsoleLogger.error(new Exception(Constants.ARTIST_NOT_FOUND));
            return null;
        }
    }

    private String handleTooManyRequestsError(String artistFullUrl, String artistResponse) {
        if (artistResponse.equals(Constants.ERROR_503)) { //too many requests error
            //try to request data again after 5 sec
            try {
                wait(5000);
            } catch (InterruptedException e) {
                ConsoleLogger.error(e);
            }
            artistResponse = requestArtistData(artistFullUrl);
        }
        return artistResponse;
    }

    private Artist prefillArtist(String artistResponse) {
        Pair<String, Artist> artistArtistLinkPair = jsonConverter.toArtist(artistResponse);
        Artist artist = artistArtistLinkPair.getObject();
        fillAlbumsCoverArts(artist);
        fillArtistPortfolio(artist, artistArtistLinkPair.getLink());
        return artist;
    }


    private void fillArtistPortfolio(Artist artist, String link) {
        //async call
        PortfolioLogicExecutor finder = new PortfolioLogicExecutor(link, artist, threadCounter);
        finder.start();
    }

    private void fillAlbumsCoverArts(Artist artist) {
        if (!artist.getAlbums().isEmpty()) {
            for (Album album : artist.getAlbums()) {
                // async call
                AlbumCoverArtLogicExecutor finder = new AlbumCoverArtLogicExecutor(album, threadCounter);
                finder.start();
            }
        }
    }

    private String requestArtistData(String fullUrl) {
        String response = Constants.EMPTY_STING;
        try {

            URL url = new URL(fullUrl);
            int statusCode = connect(url);
            if (statusCode == 200) {
                response = readResponse(url);
            } else if (statusCode == 503) {
                return Constants.ERROR_503;
            }
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
        }
        return response;
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

    private int connect(URL url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        int statusCode = http.getResponseCode();
        ConsoleLogger.log("Response received with code " + statusCode);
        return statusCode;
    }

}
