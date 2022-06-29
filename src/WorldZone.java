import java.awt.*;

public enum WorldZone {
    WEST_PARKING("WEST PARKING", Color.DARK_GRAY, 1),
    WEST_ROAD("WEST ROAD", Color.LIGHT_GRAY, 3),
    WEST_GATE("WEST GATE", Color.DARK_GRAY, 1),
    BRIDGE("BRIDGE", new Color(102, 102, 153), 3),
    EAST_GATE("EAST GATE", Color.DARK_GRAY, 1),
    EAST_ROAD("EAST ROAD", Color.LIGHT_GRAY, 3),
    EAST_PARKING("EAST PARKING", Color.DARK_GRAY, 1);

    private String name;
    private Color color;
    private int widthRatio;

    WorldZone(String name, Color color, int widthRatio) {
        this.name = name;
        this.color = color;
        this.widthRatio = widthRatio;
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

    @Override
    public String toString() {
        return name;
    }

}