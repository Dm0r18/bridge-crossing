public enum CarState {
//    INITIALIZATION("[Car has spawned on start parking] "),
//    EMBARKATION("Is embarking "),
    ON_ROAD_TO_BRIDGE("Is driving towards bridge"),
    GET_ON_BRIDGE("Is waiting in queue"),
    CROSS_THE_BRIDGE("Is crossing the bridge"),
    GET_OFF_BRIDGE("Is leaving the bridge"),
    ON_ROAD_TO_OUT("Is driving towards end"),
//    DISEMBARKATION("Is diembarking "),
    TO_REMOVE("Has despawned");

    private final String message;

    CarState(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
