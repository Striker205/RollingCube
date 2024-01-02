package sk.tuke.kpi.kp.rollingCube.service.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.rollingCube.entity.Comment;
import sk.tuke.kpi.kp.rollingCube.entity.Rating;
import sk.tuke.kpi.kp.rollingCube.service.RatingException;
import sk.tuke.kpi.kp.rollingCube.service.RatingService;

import java.util.Arrays;

public class RatingServiceRestClient implements RatingService {

    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return Integer.parseInt(restTemplate.getForEntity(url + "/" + game, String.class).getBody());
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return Integer.parseInt(restTemplate.getForEntity(url + "/" + game + "/" + player, String.class).getBody());
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
