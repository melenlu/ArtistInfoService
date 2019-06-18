package service.theads;

import service.Constants;
import service.json.model.external.Artist;

import java.io.IOException;
import java.net.URL;

public class PortfolioFinder extends BaseFinder {
    private Artist artist;
    public PortfolioFinder(String url, Artist artist,Semaphore semaphore){
        super(url,semaphore);
        this.artist=artist;
    }
    @Override
    protected String execute(URL url) throws IOException {
        String discogsResponse = super.execute(url);
        if(!discogsResponse.equals(Constants.EMPTY_STING)) {
            String description = converter.toDescription(discogsResponse);
            artist.setDescription(description);
        }
        return discogsResponse;
    }
}
