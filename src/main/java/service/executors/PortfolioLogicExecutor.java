package service.executors;

import service.Constants;
import service.json.model.external.Artist;

import java.io.IOException;
import java.net.URL;

/**
 * Logic executor of artist's portfolio
 */
public class PortfolioLogicExecutor extends BaseLogicExecutor {

    private Artist artist;

    public PortfolioLogicExecutor(String url, Artist artist, Semaphore semaphore) {
        super(url,semaphore);
        this.artist=artist;
    }

    /**
     * Execute and set artist's description in place
     *
     * @param url -external web resource url
     * @return description response string
     * @throws IOException connection exception
     */
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
