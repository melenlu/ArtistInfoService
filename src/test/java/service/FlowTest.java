package service;

import org.junit.Assert;
import org.junit.Test;
import service.executors.Semaphore;
import service.json.JsonConverter;

public class FlowTest {
    private ArtistInfoReceiver receiver = ArtistInfoReceiver.getNewInstance(new Semaphore());
    private JsonConverter converter = new JsonConverter();
    @Test
    public void receiveArtistInfoFlowTest() {
        //request from web
        long startTime = System.currentTimeMillis();
        String json = converter.toJson(receiver.getArtistInfo("7944ed53-2a58-4035-9b93-140a71e41c34"));
        long endTime = System.currentTimeMillis();
        Assert.assertTrue(endTime - startTime <= 15000);
        Assert.assertEquals("{\"mbid\":\"7944ed53-2a58-4035-9b93-140a71e41c34\",\"name\":\"Sting\",\"albums\":[{\"title\":\"Sacred Love\",\"id\":\"04ff0520-46ba-3735-be38-f53f2e4d102b\"},{\"title\":\"The Last Ship\",\"id\":\"2062ef8c-214f-40e1-a1dc-4c329a1e09cb\"},{\"title\":\"The Very Best of Sting \\u0026 The Police\",\"id\":\"27eed66f-c7ca-399b-a418-f8eebd1e0efa\"},{\"title\":\"Fields of Gold: The Best of Sting 1984–1994\",\"id\":\"33cdbfa4-86da-33fa-b7d4-67f3efcaf558\"},{\"title\":\"57th \\u0026 9th\",\"id\":\"434ec89a-50f2-4975-ab39-89b21969b811\"},{\"title\":\"Compact Hits\",\"id\":\"4fd9ed7a-3be6-44b5-b4e2-a838e349b5ec\"},{\"title\":\"Brand New Day\",\"id\":\"53989c16-f6cf-3f0c-97c3-2a718afabeeb\"},{\"title\":\"Peter and the Wolf / Classical Symphony / Overture on Hebrew Themes / March\",\"id\":\"544cb813-317b-38cc-93db-9b5211b0672d\"},{\"title\":\"Ten Summoner’s Tales\",\"id\":\"57389a2d-1570-364f-a83c-a382d0f5aafc\"},{\"title\":\"Digitally Remastered\",\"id\":\"5ba8a3ef-9e81-4e66-b7d9-414ba92890f6\"},{\"title\":\"All Thease Time\",\"id\":\"61d7c93f-4e14-4ee4-a0f6-4998fc04812e\"},{\"title\":\"Songs From the Labyrinth\",\"id\":\"6903253d-3583-367e-bd94-0bb4c5abacf2\"},{\"title\":\"Andrea Griminelli’s Cinema Italiano: A New Interpretation of Italian Film Music\",\"id\":\"6d33b935-c8a8-3933-8584-5421c8382f77\"},{\"title\":\"The Soul Cages\",\"id\":\"7ad29024-cf86-311c-9844-ca9054de16cd\"},{\"title\":\"Mercury Falling\",\"id\":\"7d919dbd-cbe6-3598-bb01-271c012d1701\"},{\"title\":\"The Dream of the Blue Turtles\",\"id\":\"93ef0ae1-0735-3699-b450-d79bdcb3d0b8\"},{\"title\":\"44/876\",\"id\":\"a8b11bb6-f8c8-4441-9887-1db5e46acce9\"},{\"title\":\"Forever Gold\",\"id\":\"ad586189-8178-3c20-ad3a-e47f9347c839\"},{\"title\":\"If on a Winter’s Night…\",\"id\":\"b10c9580-0a6d-46b0-bf8c-26a917b3c2d0\"},{\"title\":\"The Soldier\\u0027s Tale\",\"id\":\"b298b60a-f7dc-4526-ab3e-a47f6587729e\"},{\"title\":\"Greatest Hits\",\"id\":\"b5f395df-be0f-45ec-a66d-f3a76ad8757d\"},{\"title\":\"My Songs\",\"id\":\"bf242810-966f-4db7-b1ff-2f91355bbeb6\"},{\"title\":\"…Nothing Like the Sun\",\"id\":\"ec048bb5-1fc3-32a7-b560-c07ddd0df544\"},{\"title\":\"At the Movies\",\"id\":\"ee7f4e04-efff-39a6-b12f-8c00ba2895ef\"},{\"title\":\"Symphonicities\",\"id\":\"fdca52bf-f1dc-405d-8743-f88b0bc3d6eb\"}]}",
                json);

        //take from cache
        startTime = System.currentTimeMillis();
        String archievedJson = converter.toJson(receiver.getArtistInfo("7944ed53-2a58-4035-9b93-140a71e41c34"));
        endTime = System.currentTimeMillis();
        Assert.assertTrue(endTime - startTime <= 20);
        Assert.assertEquals("{\"mbid\":\"7944ed53-2a58-4035-9b93-140a71e41c34\",\"name\":\"Sting\",\"albums\":[{\"title\":\"Sacred Love\",\"id\":\"04ff0520-46ba-3735-be38-f53f2e4d102b\"},{\"title\":\"The Last Ship\",\"id\":\"2062ef8c-214f-40e1-a1dc-4c329a1e09cb\"},{\"title\":\"The Very Best of Sting \\u0026 The Police\",\"id\":\"27eed66f-c7ca-399b-a418-f8eebd1e0efa\"},{\"title\":\"Fields of Gold: The Best of Sting 1984–1994\",\"id\":\"33cdbfa4-86da-33fa-b7d4-67f3efcaf558\"},{\"title\":\"57th \\u0026 9th\",\"id\":\"434ec89a-50f2-4975-ab39-89b21969b811\"},{\"title\":\"Compact Hits\",\"id\":\"4fd9ed7a-3be6-44b5-b4e2-a838e349b5ec\"},{\"title\":\"Brand New Day\",\"id\":\"53989c16-f6cf-3f0c-97c3-2a718afabeeb\"},{\"title\":\"Peter and the Wolf / Classical Symphony / Overture on Hebrew Themes / March\",\"id\":\"544cb813-317b-38cc-93db-9b5211b0672d\"},{\"title\":\"Ten Summoner’s Tales\",\"id\":\"57389a2d-1570-364f-a83c-a382d0f5aafc\"},{\"title\":\"Digitally Remastered\",\"id\":\"5ba8a3ef-9e81-4e66-b7d9-414ba92890f6\"},{\"title\":\"All Thease Time\",\"id\":\"61d7c93f-4e14-4ee4-a0f6-4998fc04812e\"},{\"title\":\"Songs From the Labyrinth\",\"id\":\"6903253d-3583-367e-bd94-0bb4c5abacf2\"},{\"title\":\"Andrea Griminelli’s Cinema Italiano: A New Interpretation of Italian Film Music\",\"id\":\"6d33b935-c8a8-3933-8584-5421c8382f77\"},{\"title\":\"The Soul Cages\",\"id\":\"7ad29024-cf86-311c-9844-ca9054de16cd\"},{\"title\":\"Mercury Falling\",\"id\":\"7d919dbd-cbe6-3598-bb01-271c012d1701\"},{\"title\":\"The Dream of the Blue Turtles\",\"id\":\"93ef0ae1-0735-3699-b450-d79bdcb3d0b8\"},{\"title\":\"44/876\",\"id\":\"a8b11bb6-f8c8-4441-9887-1db5e46acce9\"},{\"title\":\"Forever Gold\",\"id\":\"ad586189-8178-3c20-ad3a-e47f9347c839\"},{\"title\":\"If on a Winter’s Night…\",\"id\":\"b10c9580-0a6d-46b0-bf8c-26a917b3c2d0\"},{\"title\":\"The Soldier\\u0027s Tale\",\"id\":\"b298b60a-f7dc-4526-ab3e-a47f6587729e\"},{\"title\":\"Greatest Hits\",\"id\":\"b5f395df-be0f-45ec-a66d-f3a76ad8757d\"},{\"title\":\"My Songs\",\"id\":\"bf242810-966f-4db7-b1ff-2f91355bbeb6\"},{\"title\":\"…Nothing Like the Sun\",\"id\":\"ec048bb5-1fc3-32a7-b560-c07ddd0df544\"},{\"title\":\"At the Movies\",\"id\":\"ee7f4e04-efff-39a6-b12f-8c00ba2895ef\"},{\"title\":\"Symphonicities\",\"id\":\"fdca52bf-f1dc-405d-8743-f88b0bc3d6eb\"}]}",
                archievedJson);
    }

    @Test
    public void errorFlowTest() {
        String json = converter.toJson(receiver.getArtistInfo("wrong_id"));
        Assert.assertEquals(Constants.EMPTY_STING, json);
    }
}
