package service;

import service.json.JsonConverter;
import service.json.model.external.Album;
import service.json.model.external.Artist;
import service.json.model.external.Pair;
import service.theads.AlbumCoverArtFinder;
import service.theads.BaseFinder;
import service.theads.PortfolioFinder;
import service.theads.Semaphore;
import service.util.ConsoleLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ArtistInfoReceiver {
    JsonConverter jsonConverter = new JsonConverter();
    Semaphore threadCounter;
    public ArtistInfoReceiver(Semaphore threadCounter){
        this.threadCounter=threadCounter;
    }
   // String id = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
    //"f27ec8db-af05-4f36-916e-3d57f91ecf5e?&fmt=json&inc=url-rels+release-groups";
    public static ArtistInfoReceiver getNewInstance(Semaphore threadCounter) {
        return new ArtistInfoReceiver(threadCounter);
    }

    public String getArtistInfoInJSON(String id) {
        ArtistsCache cache = ArtistsCache.getInstance();
        Artist cachedValue = cache.getArtist(id);
        if (cachedValue != null) {
            return jsonConverter.toJson(cachedValue);
        } else {
            String artistFullUrl = Constants.artistUrl + id + Constants.urlParams;//use string builder
            String artistResponse = requestArtistData(artistFullUrl);
            if (!artistFullUrl.equals(Constants.EMPTY_STING)) {
                Pair<String, Artist> artistArtistLinkPair = jsonConverter.toArtist(artistResponse);
                Artist artist = artistArtistLinkPair.getObject();

                fillAlbumsCoverArts(artist);
                fillArtistPortfolio(artist, artistArtistLinkPair.getLink());
                //caching received artist
                cache.putArtist(artist);
                //return json representation of the artist
                return jsonConverter.toJson(artist);
            } else {
                ConsoleLogger.error(new Exception(Constants.ARTIST_NOT_FOUND));
                return Constants.ARTIST_NOT_FOUND;
            }
        }
    }

    private void fillArtistPortfolio(Artist artist, String link) {
        //async call
        PortfolioFinder finder = new PortfolioFinder(link, artist,threadCounter);
        finder.start();

    }

    private void fillAlbumsCoverArts(Artist artist) {
        if (!artist.getAlbums().isEmpty()) {
            for (Album album : artist.getAlbums()) {
                // async call
                AlbumCoverArtFinder finder = new AlbumCoverArtFinder(album,threadCounter);
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
        String response = Constants.EMPTY_STING;
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            response += scanner.nextLine();
        }
        scanner.close();
        return response;
    }

    private int connect(URL url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        int statusCode = http.getResponseCode();
        ConsoleLogger.log("Response received with code " + statusCode);
        return statusCode;
    }

}
