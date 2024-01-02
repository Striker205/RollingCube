package sk.tuke.kpi.kp.rollingCube.service.jpa;

import sk.tuke.kpi.kp.rollingCube.entity.Rating;
import sk.tuke.kpi.kp.rollingCube.service.RatingException;
import sk.tuke.kpi.kp.rollingCube.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.createNamedQuery("Rating.deleteRating")
                        .setParameter("player", rating.getPlayer()).executeUpdate();
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> rating = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getResultList();

        float sum = 0f;
        for (Rating rate : rating) {
            sum += rate.getRating();
        }

        return Math.round(sum/rating.size());
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        List<Rating> rating = entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", player).getResultList();

        for(Rating rate:rating) {
            if(Objects.equals(rate.getPlayer(), player))
                return rate.getRating();
        }
        return 0;
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
