package service.json.model.internal;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.LinkedList;

public class Person {
    String id;
    String name;


    @SerializedName("release-groups")
    private Collection<Album> albums = new LinkedList<>();
    private Collection<Discogs> relations =new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    public Collection<Discogs> getRelations() {
        return relations;
    }

    public void setRelations(Collection<Discogs> relations) {
        this.relations = relations;
    }
}
