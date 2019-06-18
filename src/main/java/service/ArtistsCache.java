package service;
import service.json.model.external.Artist;
import service.util.ConsoleLogger;

import java.util.*;

public class ArtistsCache {
    private static ArtistsCache cacheInstance =  new ArtistsCache();
    public static ArtistsCache getInstance(){return cacheInstance;};

    private static Map<String, Artist> cache = Collections.synchronizedMap(new LinkedHashMap<String,Artist>());
    private final static int CAPACITY=10000;

    public Artist getArtist(String id){
        if(cache.containsKey(id)){
            return cache.get(id);
        }
        return null;
    }
    public synchronized void putArtist(Artist artist){
        if(cache.size()>=CAPACITY-1){
            cache.remove(cache.entrySet().iterator().next());
        }
        cache.put(artist.getMbid(),artist);
        ConsoleLogger.log("Artist cached");
    }
}
