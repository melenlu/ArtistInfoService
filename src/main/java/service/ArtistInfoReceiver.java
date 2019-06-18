package service;

import service.json.JsonConverter;
import service.json.model.external.Album;
import service.json.model.external.Artist;
import service.json.model.external.Pair;
import service.threads.AlbumCoverArtLogicExecutor;
import service.threads.PortfolioLogicExecutor;
import service.threads.Semaphore;
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

    public String getArtistInfoInJSON(String id) {
        ArtistsCache cache = ArtistsCache.getInstance();
        Artist cachedValue = cache.getArtist(id);
        if (cachedValue != null) {
            return jsonConverter.toJson(cachedValue);
        } else {
            return receiveArtist(id);
        }
    }

    private String receiveArtist(String id) {
        String artistFullUrl = Constants.artistUrl + id + Constants.urlParams;//use string builder
        String artistResponse = requestArtistData(artistFullUrl);
        if (!artistResponse.equals(Constants.EMPTY_STING)) {
            Artist artist = prefillArtist(artistResponse);
            //caching received artist
            ArtistsCache.getInstance().putArtist(artist);
            //return json representation of the artist
            return jsonConverter.toJson(artist);
        } else {
            ConsoleLogger.error(new Exception(Constants.ARTIST_NOT_FOUND));
            return Constants.ARTIST_NOT_FOUND;
        }
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
        try {
            URL url = new URL(fullUrl);
            int statusCode = connect(url);
            if (statusCode == 200) {
                return readResponse(url);
            } else {
                return Constants.EMPTY_STING;
            }
        } catch (Exception ex) {
            ConsoleLogger.error(ex);
        }
        return Constants.EMPTY_STING;
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
