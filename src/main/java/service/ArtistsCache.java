package service;

import service.json.model.external.Artist;
import service.util.ConsoleLogger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Cache keeps 10 000 loaded artists.
 * Provides access to artist by id.
 * Each newly loaded artist replace one old artist object.
 */
class ArtistsCache {
    private static boolean isActive = true;
    private static ArtistsCache cacheInstance = new ArtistsCache();

    static ArtistsCache getInstance() {
        return cacheInstance;
    }

    private static ConcurrentHashMap<String, Artist> cache = new ConcurrentHashMap<>();
    private final static int CAPACITY = 100000;

    static boolean isActive() {
        return isActive;
    }

    static void deactivate() {
        isActive = false;
    }

    Artist getArtist(String id) {
        if (isActive && cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;
    }

    synchronized void putArtist(Artist artist) {
        if (isActive) {
            if (cache.size() >= CAPACITY - 1) {
                cache.remove(cache.entrySet().iterator().next());
            }
            cache.put(artist.getMbid(), artist);
            ConsoleLogger.log("Artist cached");
        }
    }
}
