package service.json;


import com.google.gson.Gson;

import service.Constants;
import service.json.model.internal.*;
import service.json.model.external.Artist;
import service.json.model.external.Pair;


import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

public class JsonConverter {
    Gson gson = new Gson();
    public Pair<String,Artist> toArtist(String jsonData){
        Person person = gson.fromJson(jsonData, (Type) Person.class);
        Artist artist = new Artist(person.getId(),person.getName());
        for (Album jsonAlbum:person.getAlbums()) {
            artist.addAlbum(new service.json.model.external.Album(jsonAlbum.getId(),jsonAlbum.getTitle()));
        }
        String link= Constants.EMPTY_STING;
        if(Objects.nonNull(person.getRelations()) && person.getRelations().size()>0){
            Optional<Discogs> discogs = person.getRelations().stream().filter(d->d.getUrl().getResource().contains("discogs")).findAny();
            if(discogs.isPresent()){
                link=discogs.get().getUrl().getResource();
                link = transformLink(link);
            }
        }
        return new Pair<>(link,artist);
    }

    private String transformLink(String link) {
        link=link.replace("discogs","api.discogs");
        link=link.replace("artist","artists");
        link=link.replace("www.", Constants.EMPTY_STING);
        return link;
    }

    public String toJson(Object object){
        return gson.toJson(object);
    }
    public String toImageUrl(String jsonData){
        Cover cover=  gson.fromJson(jsonData, (Type) Cover.class);
        if(Objects.nonNull(cover)&& Objects.nonNull(cover.getImages())&&
                cover.getImages().size()>0){
        return cover.getImages().iterator().next().getImage();
        }
        else return Constants.EMPTY_STING;
    }

    public String toDescription(String jsonData){
        Description desc= gson.fromJson(jsonData, Description.class);
        if(Objects.nonNull(desc)){
            return desc.getProfile();
        }
        return Constants.EMPTY_STING;
    }
}
