package sk.tuke.kpi.kp.rollingCube.service;

import sk.tuke.kpi.kp.rollingCube.entity.User;

public interface UserService {
    void registration(User user) throws UserException;
    boolean isValid(String login, String password) throws UserException;
    void saveLevel(String login, int level) throws UserException;
    int loadLevel(String login) throws UserException;

    void reset() throws UserException;
}
