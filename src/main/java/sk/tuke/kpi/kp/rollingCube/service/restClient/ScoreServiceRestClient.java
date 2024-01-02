package sk.tuke.kpi.kp.rollingCube.service.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.service.ScoreException;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    private final String url = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url, score, Score.class);
    }

    @Override
    public List<Score> getTopScoresInGame(String game) throws ScoreException {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + game, Score[].class).getBody());
    }

    @Override
    public List<Score> getTopScoresInLevel(String game, int level) throws ScoreException {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + game + "/" + level, Score[].class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
