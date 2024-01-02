package sk.tuke.kpi.kp.rollingCube.service.jdbc;

import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.service.ScoreException;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "admin";
    public static final String SELECT_GAME = "SELECT player, game, levels, points, played_at FROM score WHERE game = ? ORDER BY points LIMIT 10";
    public static final String SELECT_LEVEL = "SELECT player, game, levels, points, played_at FROM score WHERE levels = ? ORDER BY points LIMIT 10";
    public static final String DELETE = "DELETE FROM score";
    public static final String INSERT = "INSERT INTO score (player, game, levels, points, played_at) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void addScore(Score score) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, score.getPlayer());
            statement.setString(2, score.getGame());
            statement.setInt(3, score.getLevel());
            statement.setInt(4, score.getPoints());
            statement.setTimestamp(5, new Timestamp(score.getPlayedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScoresInGame(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_GAME);
        ) {
            statement.setString(1, game);
            return getScores(statement);
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public List<Score> getTopScoresInLevel(String game, int level) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_LEVEL);
        ) {
            statement.setInt(1, level);
            return getScores(statement);
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    private List<Score> getScores(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            List<Score> scores = new ArrayList<>();
            while (rs.next()) {
                scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getTimestamp(5)));
            }
            return scores;
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}
