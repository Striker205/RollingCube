package sk.tuke.kpi.kp.rollingCube.game.core;

import java.awt.*;

public class MazeField {
    private final Level level;
    private Map map;
    private Cube cube;
    private int tmpFieldValue;
    private final Point dicePosition;
    private GameState gameState = GameState.PLAYING;
    private GameState moveState = GameState.CORRECT_MOVE;
    private int currentFileNum;
    private int highestFileNum;
    private String nickname;
    private long startMillis;
    private long pauseTime;
    private boolean endOfGame;
    private boolean startLevel;

    public MazeField() {
        this.currentFileNum = 10;
        this.highestFileNum = 1;
        this.tmpFieldValue = 0;
        this.level = new CreateLevel();
        setMap(levelLoader("Levels/Level" + getCurrentFileNum() + ".txt"));
        generateDice();
        insertDiceOnField();
        this.dicePosition = new Point(getMap().getStart().x, getMap().getStart().y);
        this.nickname = null;
        this.startMillis = 0L;
        this.endOfGame = false;
        this.pauseTime = 0L;
        this.startLevel = true;
    }

    public void pauseGame() {
        this.pauseTime = System.currentTimeMillis() - this.startMillis;
    }

    public void resumeGame() {
        this.startMillis = System.currentTimeMillis() - (this.pauseTime + this.startMillis);
    }

    public void loadNextLevel() {
        if(getHighestFileNum() <= getCurrentFileNum()) {
            setHighestFileNum(getCurrentFileNum() + 1);
        }

        if(getCurrentFileNum() < getMap().folderFileSum()) {
            setCurrentFileNum(getCurrentFileNum() + 1);
        }else {
            setCurrentFileNum(getCurrentFileNum());
        }
        setMap(levelLoader("Levels/Level" + getCurrentFileNum() + ".txt"));
        prepareLevel();
    }

    public void loadLevel(int level) {
        setCurrentFileNum(level);
        setMap(levelLoader("Levels/Level" + level + ".txt"));
        prepareLevel();
    }

    public void prepareLevel() {
        generateDice();
        insertDiceOnField();
        getDicePosition().setLocation(getMap().getStart().x, getMap().getStart().y);
        gameState = GameState.PLAYING;
        this.startMillis = System.currentTimeMillis();
    }

    public void CubeMove(String direction) {
        if(isStartLevel()) {
            this.startMillis = System.currentTimeMillis();
            setStartLevel(false);
        }

        setValueToField(getTmpFieldValue(), getDicePosition().x, getDicePosition().y);
        changeDicePosition(direction);
        saveValueFromField(getMap().getField()[getDicePosition().x][getDicePosition().y]);
        setValueToField(getCube().getTop(), getDicePosition().x, getDicePosition().y);

        if(getMoveState() == GameState.CORRECT_MOVE)
            getCube().update(direction);
        if(isSolved()) {
            setStartLevel(true);
            gameState = GameState.SOLVED;
            setEndOfGame(getCurrentFileNum() == getMap().folderFileSum());
        }
    }

    public void resetDiceToStart() {
        getCube().update("R");
        setValueToField(getTmpFieldValue(), getDicePosition().x, getDicePosition().y);
        insertDiceOnField();
        getDicePosition().setLocation(getMap().getStart().x,getMap().getStart().y);
    }

    public int getScore() {
        return gameState == GameState.SOLVED ? (int)((System.currentTimeMillis() - startMillis) / 1000) : 0;
    }

    private void changeDicePosition(String direction) {
        switch (direction) {
            case "A":
                rotateLeft();
                break;
            case "D":
                rotateRight();
                break;
            case "W":
                rotateUp();
                break;
            case "S":
                rotateDown();
                break;
            default: break;
        }
    }

    private Boolean isSolved() {
        return getDicePosition().equals(getMap().getEnd());
    }

    private Boolean isCorrectMove(int row, int column) {
        return getCube().getTop() == getMap().getField()[row][column] || (getMap().getField()[row][column] == 0);
    }

    private void insertDiceOnField() {
        saveValueFromField(getMap().getField()[getMap().getStart().x][getMap().getStart().y]);
        setValueToField(getCube().getTop(), getMap().getStart().x, getMap().getStart().y);
    }

    private void generateDice() {
        this.cube = new CubeUpdate(
                getMap().getDice().get(0),
                getMap().getDice().get(1),
                getMap().getDice().get(2),
                getMap().getDice().get(3),
                getMap().getDice().get(4)
        );
    }

    private Map levelLoader(String fileName) {
        return getLevel().loadLevel(fileName);
    }

    private void rotateDown() {
        if(getDicePosition().x+1 <= getMap().getRowCount()-1)
            if(isCorrectMove(getDicePosition().x+1,getDicePosition().y)) {
                getDicePosition().setLocation(getDicePosition().x+1,getDicePosition().y);
                moveState = GameState.CORRECT_MOVE;
            } else
                moveState = GameState.NO_POSSIBLE_MOVE;
        else
            moveState = GameState.NO_POSSIBLE_MOVE;
    }

    private void rotateUp() {
        if(getDicePosition().x-1 >= 0)
            if(isCorrectMove(getDicePosition().x-1,getDicePosition().y)) {
                getDicePosition().setLocation(getDicePosition().x-1,getDicePosition().y);
                moveState = GameState.CORRECT_MOVE;
            } else
                moveState = GameState.NO_POSSIBLE_MOVE;
        else
            moveState = GameState.NO_POSSIBLE_MOVE;
    }

    private void rotateRight() {
        if(getDicePosition().y+1 <= getMap().getColumnCount()-1)
            if(isCorrectMove(getDicePosition().x,getDicePosition().y+1)) {
                getDicePosition().setLocation(getDicePosition().x,getDicePosition().y+1);
                moveState = GameState.CORRECT_MOVE;
            } else
                moveState = GameState.NO_POSSIBLE_MOVE;
        else
            moveState = GameState.NO_POSSIBLE_MOVE;
    }

    private void rotateLeft() {
        if(getDicePosition().y-1 >= 0)
            if(isCorrectMove(getDicePosition().x,getDicePosition().y-1)) {
                getDicePosition().setLocation(getDicePosition().x, getDicePosition().y-1);
                moveState = GameState.CORRECT_MOVE;
            } else
                moveState = GameState.NO_POSSIBLE_MOVE;
        else
            moveState = GameState.NO_POSSIBLE_MOVE;
    }

    /*--------------------------------Getters/Setters----------------------------------------------*/
    private void setValueToField(int newValue, int row, int column) {
        getMap().getField()[row][column] = newValue;
    }

    private void saveValueFromField(int tmpFieldValue) {
        this.tmpFieldValue = tmpFieldValue;
    }

    public Map getMap() {
        return map;
    }
    public Cube getCube() {
        return cube;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Level getLevel() {
        return level;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getTmpFieldValue() {
        return tmpFieldValue;
    }

    public Point getDicePosition() {
        return dicePosition;
    }

    public GameState getMoveState() {
        return moveState;
    }

    public int getCurrentFileNum() {
        return currentFileNum;
    }

    private void setCurrentFileNum(int currentFileNum) {
        this.currentFileNum = currentFileNum;
    }

    public int getHighestFileNum() {
        return highestFileNum;
    }

    public void setHighestFileNum(int highestFileNum) {
        this.highestFileNum = highestFileNum;
    }

    public long getTime() {
        return !isStartLevel() ? (System.currentTimeMillis() - startMillis) / 1000 : 0L;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isEndOfGame() {
        return endOfGame;
    }

    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
    }

    private boolean isStartLevel() {
        return startLevel;
    }

    private void setStartLevel(boolean startLevel) {
        this.startLevel = startLevel;
    }
}
