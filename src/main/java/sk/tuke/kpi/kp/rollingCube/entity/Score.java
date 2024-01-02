package sk.tuke.kpi.kp.rollingCube.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@NamedQuery( name = "Score.getTopScoresInGame",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points")
@NamedQuery( name = "Score.getTopScoresInLevel",
        query = "SELECT s FROM Score s WHERE s.game=:game AND s.levels=:level ORDER BY s.points")
@NamedQuery( name = "Score.resetScores",
        query = "DELETE FROM Score")

public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;

    private String player;
    private int levels;

    private int points;

    private Date playedOn;

    public Score() {}

    public Score(String player, String game, int levels, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.levels = levels;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getLevel() {
        return levels;
    }

    public void setLevel(int level) {
        this.levels = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", level='" + levels + '\'' +
                ", points=" + points +
                ", playedOn=" + playedOn +
                '}';
    }

    public int getIdent() {
        return ident;
    }
    public void setIdent(int ident) {
        this.ident = ident;
    }
}
