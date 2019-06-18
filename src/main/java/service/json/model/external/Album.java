package service.json.model.external;

/**
 * Album entity
 */
public class Album {

    String title;
    String id;
    String Image;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public Album(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
