package sk.tuke.kpi.kp.rollingCube.service.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.rollingCube.entity.User;
import sk.tuke.kpi.kp.rollingCube.service.UserException;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

public class UserServiceRestClient implements UserService {

    private final String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void registration(User user) throws UserException {
        restTemplate.postForEntity(url,user, User.class);
    }

    @Override
    public boolean isValid(String login, String password) throws UserException {
        System.out.println(url + "?login=" + login + "&password=" + password);
        return Boolean.parseBoolean(restTemplate.getForEntity(url + "?login=" + login + "&password=" + password, String.class).getBody());
    }

    @Override
    public void saveLevel(String login, int level) throws UserException {
        restTemplate.put(url + "/" + login + "?level=" + level, User.class);
    }

    @Override
    public int loadLevel(String login) throws UserException {
        return Integer.parseInt(restTemplate.getForEntity(url + "/" + login, String.class).getBody());
    }

    @Override
    public void reset() throws UserException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
