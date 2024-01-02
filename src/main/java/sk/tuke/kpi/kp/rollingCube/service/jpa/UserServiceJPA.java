package sk.tuke.kpi.kp.rollingCube.service.jpa;

import sk.tuke.kpi.kp.rollingCube.entity.User;
import sk.tuke.kpi.kp.rollingCube.service.UserException;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional
public class UserServiceJPA implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void registration(User user) throws UserException {
        List<User> users = entityManager.createNamedQuery(
                "User.registration", User.class)
        .setParameter("login", user.getLogin()).getResultList();

        if(!users.isEmpty()) {
            throw new UserException("User with this login already exists.");
        }else {
            entityManager.persist(user);
        }
    }

    @Override
    public boolean isValid(String login, String password) throws UserException {
        List<User> users = entityManager.createNamedQuery(
                        "User.login", User.class)
                .setParameter("login", login).setParameter("password", password).getResultList();

        for(User user:users) {
            if(Objects.equals(user.getLogin(), login) && Objects.equals(user.getPassword(), password))
                return true;
        }
        return false;
    }

    @Override
    public void saveLevel(String login, int level) throws UserException {
        int result = entityManager.createNamedQuery("User.save").setParameter("login", login)
                .setParameter("level", level).executeUpdate();

        if (result == 0) {
            throw new UserException("User not found.");
        }
    }

    @Override
    public int loadLevel(String login) throws UserException {
        var level = entityManager.createNamedQuery("User.load").setParameter("login", login)
                        .getSingleResult();

        if (level == null) {
            throw new UserException("User not found.");
        }

        return Integer.parseInt(level.toString());
    }

    @Override
    public void reset() throws UserException {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
