package service;

import org.junit.Assert;
import org.junit.Test;
import service.json.model.external.Artist;

public class ArtistsCacheTest {
    private ArtistsCache cache = ArtistsCache.getInstance();

    @Test
    public void putArtistToCache() {
        Artist artist = new Artist("1", "Vova");
        cache.putArtist(artist);
        //artist is in the cache
        Assert.assertEquals(artist, cache.getArtist("1"));
    }

}