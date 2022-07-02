import java.awt.*;

public enum WorldArea {
    WEST_ROAD("WEST ROAD", Color.LIGHT_GRAY, 3,2000),
    WEST_GATE("WEST GATE", Color.DARK_GRAY, 1,1000),
    BRIDGE("BRIDGE", new Color(102, 102, 153), 3,1000),
    EAST_GATE("EAST GATE", Color.DARK_GRAY, 1,1000),
    EAST_ROAD("EAST ROAD", Color.LIGHT_GRAY, 3,2000);

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