package sk.tuke.kpi.kp.rollingCube.game.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.kpi.kp.rollingCube.entity.User;
import sk.tuke.kpi.kp.rollingCube.game.core.MazeField;
import sk.tuke.kpi.kp.rollingCube.entity.Comment;
import sk.tuke.kpi.kp.rollingCube.entity.Rating;
import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.game.core.GameState;
import sk.tuke.kpi.kp.rollingCube.service.RatingService;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;
import sk.tuke.kpi.kp.rollingCube.service.CommentService;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUi {
    public static final String GREEN_TX = "\u001B[32m";
    public static final String PURPLE_TX = "\u001B[35m";
    public static final String RED_BG = "\u001B[41m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String WHITE_BG = "\u001B[47m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String BLACK_TX = "\u001B[30m";
    public static final String RESET = "\u001B[0m";
    private final MazeField mazeField;
    private final Scanner scanner = new Scanner(System.in);
    private String line;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;

    private String login;
    private String password;


    public ConsoleUi(MazeField mazeField) {
        this.mazeField = mazeField;
        //printMenu();
    }

    private void registration() {
        System.out.print("REGISTRATION");
        System.out.print("\nEnter Login: ");
        login = scanner.nextLine().trim();

        System.out.print("\nEnter Password: ");
        password = scanner.nextLine().trim();

        userService.registration(new User(login, password));

        System.out.println("Welcome to the CubeMaze " + getMazeField().getNickname());
        System.out.print("\n");
        line = scanner.nextLine().trim();
    }

    private void login() {
        System.out.print("LOGIN\n");
        System.out.print("Login: ");
        login = scanner.nextLine().trim();

        System.out.print("Password: ");
        password = scanner.nextLine().trim();

        if(userService.isValid(login, password)) {
            System.out.println("Login Successful");
        }else {
            System.out.println("RIP");
            System.exit(1);
        }
    }

    private void saveLevelToDatabase() {
        System.out.println("Do you want to save your progress? [y/n]");
        var line = scanner.nextLine().trim();

        if(line.equals("y")) {
            userService.saveLevel(login, getMazeField().getCurrentFileNum());
            System.out.println("progress saved");
        }
    }

    private int loadLevelFromDatabase() {
        System.out.println("Do you want to load your progress? [y/n]");
        var line = scanner.nextLine().trim();

        if(line.equals("y")) {
            return userService.loadLevel(login);
        }
        return 1;
    }


    public void play() {
        //registration();
        login();
        getMazeField().loadLevel(loadLevelFromDatabase());

        System.out.println(GREEN_TX + "MOVEMENT: " + RESET + "w-s-a-d " + GREEN_TX + "RESET LEVEL: " + RESET + "r " + GREEN_TX + "EXIT: " + RESET + "x ");
        System.out.println(GREEN_TX + "GAME SCORE: " + RESET + "g " + GREEN_TX + "LEVEL SCORE: " + RESET + "l");
        System.out.println(GREEN_TX + "AVERAGE RATING: " + RESET + "p " + GREEN_TX + "COMMENTS TO THE GAME: " + RESET + "c");

        /*
        if(getMazeField().getFileNum() == 2)
            ratingGame();
        */
        do {
            printField();
            processInput();
        } while(getMazeField().getGameState() == GameState.PLAYING);
        printField();
        System.out.println("YOU BEAT LEVEL!");

        CheckEndOfGame();
        EndOfLevel();
    }

    private void EndOfLevel() {
        //saveLevelToDatabase();

        saveScore();
        playCompletedLevels();

        do {
            System.out.println("NEXT LEVEL? [y/n]");
            line = scanner.nextLine().toUpperCase().trim();
            switch(line) {
                case "Y": getMazeField().loadNextLevel();
                    play();
                    break;
                case "L": printLevelScore(getMazeField().getCurrentFileNum());
                    break;
                case "G": printGameScore();
                    break;
                case "C" : printComments();
                    break;
                case "P" : printAverageRating();
                    break;
                default: break;
            }
        }while(!line.equals("N"));
    }

    private void CheckEndOfGame() {
        if(getMazeField().getMap().folderFileSum() == getMazeField().getCurrentFileNum()) {
            System.out.println("YOU COMPLETED ALL LEVELS, CONGRATULATION!");
            playCompletedLevels();
            System.exit(0);
        }
    }

    private void playCompletedLevels() {
        Pattern pattern = Pattern.compile("[1-" + getMazeField().getCurrentFileNum() + "]");

        System.out.println("REPEAT ANY COMPLETED LEVEL? [1-" + getMazeField().getHighestFileNum() + "/n]");
        line = scanner.nextLine().toUpperCase().trim();
        var match = pattern.matcher(line);
        if(match.matches()) {
            getMazeField().loadLevel(match.group(0).charAt(0) - '0');
            play();
        }
    }

    private void processInput() {
        System.out.print("\nEnter command: ");
        line = scanner.nextLine().toUpperCase().trim();

        switch(line){
            case "X": System.exit(0);
                break;
            case "R": getMazeField().resetDiceToStart();
                break;
            case "L": printLevelScore(getMazeField().getCurrentFileNum());
                break;
            case "G": printGameScore();
                break;
            case "C" : printComments();
                break;
            case "P" : printAverageRating();
                break;
            default: break;
        }

        getMazeField().CubeMove(line);
    }

    private void ratingGame() {
        System.out.print("\nCOMMENT THIS GAME (Optional): ");
        line = scanner.nextLine().trim();
        if(!line.equals(""))
            saveComment(line);

        System.out.print("RATING THIS GAME [0-5]: ");
        int rate = scanner.nextInt();

        if(rate >= 0 && rate <= 5)
            saveRating(rate);
    }

    private void printField() {
        printCube();
        printMap();
    }

    private void printMap() {
        System.out.println(PURPLE_TX + "Level: " + RESET + getMazeField().getCurrentFileNum());
        for(int i = 0; i<getMazeField().getMap().getRowCount(); i++) {
            for(int j = 0; j<getMazeField().getMap().getColumnCount(); j++) {

                if(getMazeField().getDicePosition().x == i && getMazeField().getDicePosition().y == j) {
                    System.out.print(BLUE_BG + " " + BLACK_TX + getMazeField().getCube().getTop() + " " + RESET);
                }
                else if(getMazeField().getMap().getEnd().x == i && getMazeField().getMap().getEnd().y == j) {
                    System.out.print(RED_BG + " " + BLACK_TX + getMazeField().getMap().getField()[i][j] + " " + RESET);
                }
                else if((i+j)%2 == 0) {
                    System.out.print(YELLOW_BG + " " + BLACK_TX + getMazeField().getMap().getField()[i][j] + " " + RESET);
                }else {
                    System.out.print(WHITE_BG + " " + BLACK_TX + getMazeField().getMap().getField()[i][j] + " " + RESET);
                }
            }
            System.out.println();
        }
    }

    private void printCube() {
        System.out.println(PURPLE_TX + "Game state: " + RESET + getMazeField().getGameState());
        System.out.println(PURPLE_TX + "Move state: " + RESET + getMazeField().getMoveState());

        System.out.println("\t" + BLUE_BG + " " + BLACK_TX + getMazeField().getCube().getBack() + " " + RESET);

        System.out.print(" " + BLUE_BG + " " + BLACK_TX + getMazeField().getCube().getLeft() + " " + RESET);
        System.out.print(BLUE_BG + " " + BLACK_TX + getMazeField().getCube().getTop() + " " + RESET);
        System.out.print(BLUE_BG + " " + BLACK_TX + getMazeField().getCube().getRight() + " " + RESET);
        System.out.print("\n");

        System.out.println("\t" + BLUE_BG + " "  + BLACK_TX + getMazeField().getCube().getFront() + " " + RESET);
        System.out.print("\n");
    }

    private void printGameScore() {
        var scores = scoreService.getTopScoresInGame("CubeMaze");
        System.out.println("GAME SCORE:");
        System.out.println("------------------------------------------------");
        for(int i=0; i<scores.size(); i++) {
            var score = scores.get(i);
            var rating = ratingService.getRating("CubeMaze", score.getPlayer());
            System.out.printf("%2d. %-6s  %1d.level   %1ds \t %s \t %1d" + " " + "\u2605" + "\n", (i+1), score.getPlayer(), score.getLevel(), score.getPoints(), score.getPlayedOn(), rating);
        }
        System.out.println("------------------------------------------------");
        line = scanner.nextLine().toUpperCase().trim();
    }

    private void printLevelScore(int level) {
        var scores = scoreService.getTopScoresInLevel("CubeMaze", level);
        System.out.println("LEVEL SCORE:");
        System.out.println("-------------------------------------------------");
        for(int i=0; i<scores.size(); i++) {
            var score = scores.get(i);
            System.out.printf("%2d. %-6s   %1ds \t %s\n", (i+1), score.getPlayer(), score.getPoints(), score.getPlayedOn());
        }
        System.out.println("-------------------------------------------------");
        line = scanner.nextLine().toUpperCase().trim();
    }

    private void printComments() {
        var comments = commentService.getComments("CubeMaze");
        System.out.println("COMMENTS:");
        System.out.println("-----------------------------------------------");
        for(int i=0; i<comments.size(); i++) {
            var comment = comments.get(i);
            System.out.printf("%2d. %-6s  %2s     %s\n", (i+1), comment.getPlayer(), comment.getComment(), comment.getCommentedOn());
        }
        System.out.println("-----------------------------------------------");
        line = scanner.nextLine().toUpperCase().trim();
    }

    private void printAverageRating() {
        var rating = ratingService.getAverageRating("CubeMaze");
        System.out.println("AVERAGE RATING:");
        System.out.println("----------------------------------------------");
        System.out.println("rating: " + rating + " " + "\u2605");
        System.out.println("----------------------------------------------");
        line = scanner.nextLine().toUpperCase().trim();
    }

    private void saveScore() {
        scoreService.addScore(
                new Score(getMazeField().getNickname(), "CubeMaze", getMazeField().getCurrentFileNum(), getMazeField().getScore(), new Date())
        );
    }

    private void saveComment(String comment) {
        commentService.addComment(
                new Comment(getMazeField().getNickname(), "CubeMaze", comment, new Date())
        );
    }

    private void saveRating(int rating) {
        ratingService.setRating(
                new Rating(getMazeField().getNickname(), "CubeMaze", rating, new Date())
        );
    }

    private void printMenu() {
        System.out.print("\nEnter Nickname: ");
        line = scanner.nextLine().trim();
        getMazeField().setNickname(line);
        System.out.println("Welcome to the CubeMaze " + getMazeField().getNickname());
        System.out.print("\n");
        line = scanner.nextLine().trim();
    }

    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public MazeField getMazeField() {
        return mazeField;
    }
}
