package service.json.model.external;

/**
 * Album entity
 */
public class Album {

    private String title;
    private String id;
    private String Image;

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
