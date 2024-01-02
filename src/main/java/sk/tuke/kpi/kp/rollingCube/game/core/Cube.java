package sk.tuke.kpi.kp.rollingCube.game.core;

public interface Cube {
    void update(String direction);
    int getTop();
    int getFront();
    int getBack();
    int getLeft();
    int getRight();

}
