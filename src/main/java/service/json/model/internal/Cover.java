package service.json.model.internal;

import java.util.Collection;

public class Cover {
    Collection<Front> images;

    public Collection<Front> getImages() {
        return images;
    }

    public void setImages(Collection<Front> images) {
        this.images = images;
    }

    public class Front{
        String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
