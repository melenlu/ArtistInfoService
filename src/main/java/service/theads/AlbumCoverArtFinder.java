package service.theads;

import service.Constants;
import service.json.model.external.Album;


import java.io.IOException;

import java.net.URL;


public class AlbumCoverArtFinder extends BaseFinder {

    private Album album;


    public AlbumCoverArtFinder(Album album,Semaphore semaphore) {
        super(Constants.albumsCoverUrl + album.getId(),semaphore);
        this.album = album;
    }

    @Override
    protected String execute(URL url) throws IOException {
        String response = super.execute(url);
        album.setImage(converter.toImageUrl(response));
        return response;
    }


}
