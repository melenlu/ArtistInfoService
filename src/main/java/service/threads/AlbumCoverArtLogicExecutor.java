package service.threads;

import service.Constants;
import service.json.model.external.Album;

import java.io.IOException;
import java.net.URL;

/**
 * Logic executor of album art link
 */
public class AlbumCoverArtLogicExecutor extends BaseLogicExecutor {

    private Album album;

    public AlbumCoverArtLogicExecutor(Album album, Semaphore semaphore) {
        super(Constants.albumsCoverUrl + album.getId(),semaphore);
        this.album = album;
    }

    /**
     * execute and set image url in place
     *
     * @param url - external web resource url
     * @return response string
     * @throws IOException connection exception
     */
    @Override
    protected String execute(URL url) throws IOException {
        String response = super.execute(url);
        if (!response.isEmpty()) {
            album.setImage(converter.toImageUrl(response));
        }
        return response;
    }


}
