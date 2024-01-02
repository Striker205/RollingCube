package sk.tuke.kpi.kp.rollingCube.game.core;

import java.util.ArrayList;
import java.util.List;

public class CubeUpdate implements Cube{
    private int top;
    private int front;
    private int back;
    private int left;
    private int right;
    private final List<Integer> saveSides;
    private int tmpSide;

    public CubeUpdate(int top, int front, int back, int left, int right) {
        this.saveSides = new ArrayList<>();
        saveDice(top, front, back, left, right);
        this.top = top;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
        this.tmpSide = 0;
    }

    private void rollDiceLeft() {
        tmpSide = 0;
        tmpSide = getTop();
        setTop(getRight());
        setLeft(tmpSide);
        setRight(getOpposite(tmpSide));
    }

    private void rollDiceRight() {
        tmpSide = 0;
        tmpSide = getTop();
        setTop(getLeft());
        setLeft(getOpposite(tmpSide));
        setRight(tmpSide);
    }

    private void rollDiceDown() {
        tmpSide = 0;
        tmpSide = getTop();
        setTop(getBack());
        setFront(tmpSide);
        setBack(getOpposite(tmpSide));
    }

    private void rollDiceUp() {
        tmpSide = 0;
        tmpSide = getTop();
        setTop(getFront());
        setFront(getOpposite(tmpSide));
        setBack(tmpSide);
    }

    private int getOpposite(int diceSide) {
        return Math.abs(diceSide - 7);
    }

    @Override
    public void update(String direction) {
        if (direction == null) {
            return;
        }
        switch (direction) {
            case "W": rollDiceUp();
                break;
            case "S": rollDiceDown();
                break;
            case "D": rollDiceRight();
                break;
            case "A": rollDiceLeft();
                break;
            case "R": resetDice();
                break;
            default: break;
        }
    }

    private void resetDice() {
        setTop(getSaveSides().get(0));
        setFront(getSaveSides().get(1));
        setBack(getSaveSides().get(2));
        setLeft(getSaveSides().get(3));
        setRight(getSaveSides().get(4));
    }

    private void saveDice(int top, int front, int back, int left, int right) {
        getSaveSides().add(top);
        getSaveSides().add(front);
        getSaveSides().add(back);
        getSaveSides().add(left);
        getSaveSides().add(right);
    }


    /*--------------------------------Getters/Setters----------------------------------------------*/
    private void setTop(int top) {
        this.top = top;
    }

    private void setFront(int front) {
        this.front = front;
    }

    private void setBack(int back) {
        this.back = back;
    }

    private void setLeft(int left) {
        this.left = left;
    }

    private void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public int getFront() {
        return front;
    }

    public int getBack() {
        return back;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public List<Integer> getSaveSides() {
        return saveSides;
    }
}
