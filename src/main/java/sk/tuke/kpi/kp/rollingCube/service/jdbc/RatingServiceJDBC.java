package sk.tuke.kpi.kp.rollingCube.service.jdbc;

import sk.tuke.kpi.kp.rollingCube.entity.Rating;
import sk.tuke.kpi.kp.rollingCube.service.RatingException;
import sk.tuke.kpi.kp.rollingCube.service.RatingService;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "admin";
    public static final String SELECT_RATING = "SELECT player, game, player_rating FROM rating WHERE game = ? AND player = ? ORDER BY rated_at";
    public static final String SELECT_AVERAGE_RATING = "SELECT round(avg(player_rating)) FROM rating WHERE game = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String DELETE_RATING = "DELETE FROM rating WHERE player = ?";
    public static final String INSERT = "INSERT INTO rating (player, game, player_rating, rated_at) VALUES (?, ?, ?, ?)";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statementInsert = connection.prepareStatement(INSERT);
             PreparedStatement statementDelete = connection.prepareStatement(DELETE_RATING)
        ) {
            statementDelete.setString(1, rating.getPlayer());
            statementDelete.executeUpdate();

            statementInsert.setString(1, rating.getPlayer());
            statementInsert.setString(2, rating.getGame());
            statementInsert.setInt(3, rating.getRating());
            statementInsert.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statementInsert.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE_RATING);
        ) {
            statement.setString(1, game);
            ResultSet rs = statement.executeQuery();

            if(rs.next())
                return rs.getInt(1);
            else
                return 0;
        } catch (SQLException e) {
            throw new RatingException("Problem selecting rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING);
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            ResultSet rs = statement.executeQuery();
            if(rs.next())
                return rs.getInt(3);
            else
                return 0;
        } catch (SQLException e) {
            throw new RatingException("Problem selecting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting rating", e);
        }
    }
}
