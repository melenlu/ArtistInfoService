package service.json.model.external;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Artist entity
 */
public class Artist {
    private String mbid;
    private String name;
    private String description;

    private Collection<Album> albums = new LinkedList<>();

    public Collection<Album> getAlbums() {
        return Collections.unmodifiableCollection(albums);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Artist(String mbid, String name) {
        this.mbid = mbid;
        this.name = name;
    }
}
