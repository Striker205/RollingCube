package sk.tuke.kpi.kp.rollingCube;

import sk.tuke.kpi.kp.rollingCube.game.consoleui.ConsoleUi;
import sk.tuke.kpi.kp.rollingCube.game.core.MazeField;
import sk.tuke.kpi.kp.rollingCube.service.*;
import sk.tuke.kpi.kp.rollingCube.service.jdbc.CommentServiceJDBC;
import sk.tuke.kpi.kp.rollingCube.service.jdbc.RatingServiceJDBC;
import sk.tuke.kpi.kp.rollingCube.service.jdbc.ScoreServiceJDBC;

public class RollingCube {
    public static void main(String[] args) {
        ScoreService scoreService = new ScoreServiceJDBC();
        CommentService commentService = new CommentServiceJDBC();
        RatingService ratingService = new RatingServiceJDBC();

        MazeField field = new MazeField();
        ConsoleUi ui = new ConsoleUi(field);

        ui.setScoreService(scoreService);
        ui.setCommentService(commentService);
        ui.setRatingService(ratingService);
        ui.play();
    }
}
