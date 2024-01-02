package sk.tuke.kpi.kp.rollingCube.service;

import sk.tuke.kpi.kp.rollingCube.entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScoresInGame(String game) throws ScoreException;
    List<Score> getTopScoresInLevel(String game, int level) throws ScoreException;
    void reset() throws ScoreException;
}
