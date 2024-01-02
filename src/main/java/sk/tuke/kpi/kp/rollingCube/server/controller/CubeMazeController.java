package sk.tuke.kpi.kp.rollingCube.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.rollingCube.entity.Comment;
import sk.tuke.kpi.kp.rollingCube.entity.Rating;
import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.game.core.GameState;
import sk.tuke.kpi.kp.rollingCube.game.core.MazeField;
import sk.tuke.kpi.kp.rollingCube.service.CommentService;
import sk.tuke.kpi.kp.rollingCube.service.RatingService;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

import java.util.Date;

@Controller
@RequestMapping("/CubeMaze")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CubeMazeController {
    @Autowired
    private UserController userController;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    private final MazeField field = new MazeField();

    @GetMapping
    public String CubeMaze(Model model) {
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/move")
    public String moveDice(@RequestParam String direction, Model model) {
        field.CubeMove(direction);

        if(field.getGameState() == GameState.SOLVED) {
            try {
                userService.saveLevel(userController.getLoggedUser().getLogin(), field.getHighestFileNum());
                scoreService.addScore(new Score(userController.getLoggedUser().getLogin(), "CubeMaze", field.getCurrentFileNum(), field.getScore(), new Date()));
            }catch (Exception e) {
                System.out.println(e);
            }
        }

        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/load")
    public String loadLevel(@RequestParam int level, Model model) {
        field.loadLevel(level);
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/commentgame")
    public String commentGame(@RequestParam(required = false) String comment, @RequestParam String rating, Model model) {
        try {
            if (userController.getLoggedUser().getLogin() != null) {
                if (comment != null) {
                    commentService.addComment(new Comment(userController.getLoggedUser().getLogin(), "CubeMaze", comment, new Date()));
                }
                ratingService.setRating(new Rating(userController.getLoggedUser().getLogin(), "CubeMaze", Integer.parseInt(rating), new Date()));
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/loadNext")
    public String loadNextLevel(Model model) {
        field.loadNextLevel();
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/loadAgain")
    public String loadLevelAgain(Model model) {
        field.loadLevel(field.getCurrentFileNum());
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/accountLoad")
    public String loadAccountLevel(@RequestParam int level, Model model) {
        field.prepareLevel();
        field.setHighestFileNum(level);
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/reset")
    public String resetDice(Model model) {
        field.resetDiceToStart();
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/end")
    public String endOfGame(Model model) {
        field.loadLevel(field.getCurrentFileNum());
        field.setEndOfGame(false);
        prepareModel(model);
        return "CubeMaze";
    }

    @GetMapping("/time")
    public ResponseEntity<String> getTime() {
        return ResponseEntity.ok(Long.toString(field.getTime()));
    }

    public int getLevelNum() {
        return field.getHighestFileNum();
    }

    private void prepareModel(Model model) {
        model.addAttribute("scoresInGame", scoreService.getTopScoresInGame("CubeMaze"));
        model.addAttribute("scoresInLevel", scoreService.getTopScoresInLevel("CubeMaze", field.getCurrentFileNum()));
        model.addAttribute("comments", commentService.getComments("CubeMaze"));
        model.addAttribute("rating", ratingService.getAverageRating("CubeMaze"));
        model.addAttribute("levelNum", field.getCurrentFileNum());
        model.addAttribute("gameState", field.getGameState());
        model.addAttribute("moveState", field.getMoveState());
        model.addAttribute("endOfGame", field.isEndOfGame());
        model.addAttribute("timeLevel", field.getTime());
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        for(int i = 0; i<field.getMap().getRowCount(); i++) {
            sb.append("<tr>\n");
            for(int j = 0; j< field.getMap().getColumnCount(); j++) {
                sb.append("<td>\n");

                if(field.getDicePosition().x == i && field.getDicePosition().y == j) {
                    sb.append(diceFaces(field.getCube().getTop(), "diceB"));
                }
                else if(field.getMap().getEnd().x == i && field.getMap().getEnd().y == j) {
                    sb.append(diceFaces(field.getMap().getField()[i][j], "diceR"));
                }
                else {
                    if ((i + j) % 2 == 0) {
                        sb.append(diceFaces(field.getMap().getField()[i][j], "diceY"));
                    } else {
                        sb.append(diceFaces(field.getMap().getField()[i][j], "diceW"));
                    }
                }

                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public String getHtmlDice() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>\n");
        diceRow1(sb);
        diceRow2(sb);
        diceRow3(sb);
        sb.append("</table>\n");

        return sb.toString();
    }

    private void diceRow3(StringBuilder sb) {
        sb.append("<tr>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(0, "diceW"));
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(field.getCube().getFront(), "diceB"));
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(0, "diceW"));
        sb.append("</td>\n");
        sb.append("</tr>\n");
    }

    private void diceRow2(StringBuilder sb) {
        sb.append("<tr>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(field.getCube().getLeft(), "diceB"));
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(field.getCube().getTop(), "diceB"));
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(field.getCube().getRight(), "diceB"));
        sb.append("</td>\n");
        sb.append("</tr>\n");
    }

    private void diceRow1(StringBuilder sb) {
        sb.append("<tr>\n");
        sb.append("<td>\n");
        sb.append("<h2 class=\"h2-dice-name\"><strong>Dice:</strong></h2>");
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(field.getCube().getBack(), "diceB"));
        sb.append("</td>\n");
        sb.append("<td>\n");
        sb.append(diceFaces(0, "diceW"));
        sb.append("</td>\n");
        sb.append("</tr>\n");
    }

    private String diceFaces(int face, String color) {
        switch(face) {
            case 1: return face1(color);
            case 2: return face2(color);
            case 3: return face3(color);
            case 4: return face4(color);
            case 5: return face5(color);
            case 6: return face6(color);
            case 0: return face0(color);
            default: return "0";
        }
    }

    private String face1(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face1\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face2(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face2\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face3(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face3\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face4(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face4\">\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face5(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face5\">\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face6(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face6\">\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("<div class=\"column\">\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("<span class=\"dot\"></span>\n");
        sb.append("</div>\n");

        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    private String face0(String color) {
        StringBuilder sb = new StringBuilder();

        sb.append("<span>");
        sb.append("<div class=\"" + color + " face1\">\n");
        sb.append("</div>\n");
        sb.append("</span>\n");

        return sb.toString();
    }

    public RatingService getRatingService() {
        return ratingService;
    }
}
