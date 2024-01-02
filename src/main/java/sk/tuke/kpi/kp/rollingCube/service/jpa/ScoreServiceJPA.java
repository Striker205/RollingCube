package sk.tuke.kpi.kp.rollingCube.service.jpa;

import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.service.ScoreException;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getTopScoresInGame(String game) throws ScoreException {
        return entityManager.createNamedQuery("Score.getTopScoresInGame")
                .setParameter("game", game).setMaxResults(10).getResultList();
    }

    @Override
    public List<Score> getTopScoresInLevel(String game, int level) throws ScoreException {
        return entityManager.createNamedQuery("Score.getTopScoresInLevel")
                .setParameter("game", game).setParameter("level", level).setMaxResults(10).getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }
}
