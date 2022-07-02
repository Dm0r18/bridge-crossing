package world;

import java.awt.*;

public enum WorldArea {
    WEST_ROAD("WEST ROAD", new Color(228,220,207), 3,2000),
    WEST_GATE("WEST GATE", new Color(125,157,156), 1,1000),
    BRIDGE("BRIDGE", new Color(87,111,114), 3,1000),
    EAST_GATE("EAST GATE", new Color(125,157,156), 1,1000),
    EAST_ROAD("EAST ROAD", new Color(228,220,207), 3,2000);

    private final String name;
    private final Color color;
    private final int widthRatio;
    private final int timeLimit;

    WorldArea(String name, Color color, int widthRatio, int timeLimit) {
        this.name = name;
        this.color = color;
        this.widthRatio = widthRatio;
        this.timeLimit = timeLimit;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public String toString() {
        return name;
    }
}