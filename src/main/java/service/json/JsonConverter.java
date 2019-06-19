package service.json;

import com.google.gson.Gson;
import service.Constants;
import service.json.model.external.Artist;
import service.json.model.external.Pair;
import service.json.model.internal.*;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * Converter uses {@link Gson} class
 * to convert object from/to JSON format string.
 * For conversion from JSON internal object model is used.
 * For conversion to JSON external model is used.
 */
public class JsonConverter {
    private Gson gson = new Gson();

    /**
     * Convert json string to artist object
     *
     * @param jsonData - json string
     * @return {@link Pair} where
     * string is link to artist portfolio,
     * Artist is artist object with prefilled albums
     */
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

    /**
     * Transforms doscogs web site link into api link
     *
     * @param link - www.discogs/artist/id
     * @return - api/artists/id
     */
    private String transformLink(String link) {
        link=link.replace("discogs","api.discogs");
        link=link.replace("artist","artists");
        link=link.replace("www.", Constants.EMPTY_STING);
        return link;
    }

    /**
     * Convert object to json string
     * @param object - external model object
     * @return JSON string object representation
     */
    public String toJson(Object object){
        if (Objects.nonNull(object))
            return gson.toJson(object);
        else return Constants.EMPTY_STING;
    }

    /**
     * Extract image url from received internal model object {@link Cover}
     * @param jsonData - cover json string
     * @return image url string if found
     * {@code EMPTY_STRING} otherwise
     */
    public String toImageUrl(String jsonData){
        Cover cover=  gson.fromJson(jsonData, (Type) Cover.class);
        if(Objects.nonNull(cover)&& Objects.nonNull(cover.getImages())&&
                cover.getImages().size() > 0) {
            return cover.getImages().iterator().next().getImage();
        } else return Constants.EMPTY_STING;
    }

    /**
     * Extract profile string from {@link Description} object
     * @param jsonData - description json string
     * @return portfolio string
     */
    public String toDescription(String jsonData){
        Description desc= gson.fromJson(jsonData, Description.class);
        if(Objects.nonNull(desc)){
            return desc.getProfile();
        }
        return Constants.EMPTY_STING;
    }
}
