package sk.tuke.kpi.kp.rollingCube.game.core;

public class CreateLevel implements Level {

    @Override
    public Map loadLevel(String fileName) {
        return new Map(fileName);
    }
}
