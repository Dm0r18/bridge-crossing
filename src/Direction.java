public enum Direction {
    EAST("E", 1),
    WEST("W", -1);

    private final String name;
    private final int direction;

    Direction(String name, int direction) {
        this.name = name;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return name;
    }
}
