import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

public class Car implements Runnable {
    private final Bridge bridge = Bridge.getInstance();

    private static int COUNTER = 1;
    private final int carID;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 20;
    private int x;
    private final int y;
    private CarState state;
    private final Color color;

    private int speed;

    private final Direction direction;
    private final WorldMap worldMap;

    public Car(WorldMap worldMap) {
        this.worldMap = worldMap;
        speed = 0;

        this.carID = COUNTER++;
        state = CarState.ON_ROAD_TO_BRIDGE;
        if(ThreadLocalRandom.current().nextBoolean()) {
            direction = Direction.EAST;
            x = 4;
            color = new Color(51, 204, 51);
         } else {
            direction = Direction.WEST;
            x = worldMap.getWidth() - WIDTH - 4;
            color =  new Color(0, 204, 255);
        }
        int worldHeight = worldMap.getHeight();
        int randomPixel  = ThreadLocalRandom.current().nextInt(0, worldHeight + 1);
        int spawnLineY =  randomPixel % (worldHeight / HEIGHT);
        y = HEIGHT * spawnLineY;
    }

    @Override
    public void run() {
        while(state != CarState.TO_REMOVE) {
            switch (state) {
                case ON_ROAD_TO_BRIDGE -> onRoadToBridge();
                case GET_ON_BRIDGE -> getOnBridge();
                case CROSS_THE_BRIDGE -> crossTheBridge();
                case GET_OFF_BRIDGE -> getOffBridge();
                case ON_ROAD_TO_OUT -> onRoadToParking();
                default -> {
                }
            }
            updatePosition();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                System.err.println("Update Position Error");
            }
        }
    }

//    private void initialization() {
//        nextState = CarState.EMBARKATION;
//        currentState = nextState;
//    }
//
//    private void embarkation() {
//        if(nextState == CarState.EMBARKATION) {
//            sendLog(currentState.toString());
//            takeNap(MIN_EMBARKATION_TIME, MAX_EMBARKATION_TIME);
//
//            nextState = CarState.ON_ROAD_TO_BRIDGE;
//            currentState = nextState;
//        }
//    }

    private void onRoadToBridge() {
        if(direction == Direction.EAST) {
            calculateSpeed(WorldArea.WEST_ROAD);
            if(worldMap.getWorldArea(x+WIDTH) == WorldArea.BRIDGE) {
                speed = 0;
                state = CarState.GET_ON_BRIDGE;
//                calculateSpeed(WorldArea.WEST_GATE);
//                state = CarState.GET_ON_BRIDGE;
            }
        } else {
            calculateSpeed(WorldArea.EAST_ROAD);
            if(worldMap.getWorldArea(x) == WorldArea.BRIDGE) {
                speed = 0;
                state = CarState.GET_ON_BRIDGE;
//                calculateSpeed(WorldArea.EAST_GATE);
//                state = CarState.GET_ON_BRIDGE;
            }
        }
//        if(state == CarState.ON_ROAD_TO_BRIDGE) {
//            System.out.println(carID + " " + state);
//            calculateSpeed();
//            if(worldMap.getWorldArea(x) == WorldArea.EAST_GATE ||
//                worldMap.getWorldArea(x) == WorldArea.WEST_GATE) {
//                    state = CarState.GET_ON_BRIDGE;
//            }
//        }else {
//            if(direction == Direction.EAST) {
//                if(x > worldMap.getWorldZoneX(WorldArea.WEST_GATE))
//                    currentState = nextState;
//            }else if(direction == Direction.WEST) {
//                if(x + WIDTH < worldMap.getWorldZoneX(WorldArea.EAST_ROAD))
//                    currentState = nextState;
//            }
//        }
    }

    private void getOnBridge() {
        bridge.getOnTheBridge(this);
        calculateSpeed(WorldArea.BRIDGE);
        state = CarState.CROSS_THE_BRIDGE;
//        if(direction == Direction.EAST) {
//            System.out.println(1);
//            if(worldMap.getWorldArea(x+WIDTH) == WorldArea.BRIDGE) {
//                System.out.println("SINISTRA");
//                bridge.getOnTheBridge(this);
//                System.out.println("Fine getON");
//                calculateSpeed(WorldArea.BRIDGE);
//                state = CarState.CROSS_THE_BRIDGE;
//                System.out.println("DA SINISTRA ");
//            }
//        } else {
//            if(worldMap.getWorldArea(x) == WorldArea.BRIDGE) {
//                bridge.getOnTheBridge(this);
//                calculateSpeed(WorldArea.BRIDGE);
//                state = CarState.CROSS_THE_BRIDGE;
//                System.out.println("DA DESTRA ");
//            }
//        }
////        calculateSpeed();
//        System.out.println("inizio");
//        bridge.getOnTheBridge(this);
//        System.out.println("fine wait");
//        state = CarState.CROSS_THE_BRIDGE;
    }

    private void crossTheBridge() {
        if(direction == Direction.EAST) {
            if(worldMap.getWorldArea(x) == WorldArea.EAST_GATE) {
                calculateSpeed(WorldArea.EAST_ROAD);
                state = CarState.GET_OFF_BRIDGE;
//                calculateSpeed(WorldArea.WEST_GATE);
//                state = CarState.GET_ON_BRIDGE;
            }
        } else {
            if(worldMap.getWorldArea(x+WIDTH) == WorldArea.WEST_GATE) {
                calculateSpeed(WorldArea.WEST_ROAD);
                state = CarState.GET_OFF_BRIDGE;
//                calculateSpeed(WorldArea.EAST_GATE);
//                state = CarState.GET_ON_BRIDGE;
            }
        }
//        if(nextState == CarState.CROSS_THE_BRIDGE) {
//            System.out.println(carID + " " + state);
//            calculateSpeed(WorldArea.BRIDGE);

//            state = CarState.GET_OFF_BRIDGE;
//        }else {
//            if(direction == Direction.EAST) {
//                if (x > worldMap.getWorldZoneX(WorldArea.EAST_GATE))
//                    currentState = nextState;
//            }else if(direction == Direction.WEST) {
//                if (x + WIDTH < worldMap.getWorldZoneX(WorldArea.BRIDGE))
//                    currentState = nextState;
//            }
//        }
    }

    private void getOffBridge() {
        bridge.getOffTheBridge(this);
        state = CarState.ON_ROAD_TO_OUT;
    }

    private void onRoadToParking() {
//        if(nextState == CarState.ON_ROAD_TO_OUT) {
//            System.out.println(carID + " " + state
            toRemove();
//            state = CarState.TO_REMOVE;
//        }else {
//            if(direction == Direction.EAST) {
//                if (x > worldMap.getWorldZoneX(WorldArea.EAST_PARKING))
//                    currentState = nextState;
//            }else if(direction == Direction.WEST) {
//                if (x + WIDTH < worldMap.getWorldZoneX(WorldArea.WEST_ROAD))
//                    currentState = nextState;
//            }
//        }
    }

    private void toRemove() {
        if(direction == Direction.EAST) {
            if (x == worldMap.getWidth() - WIDTH - 4) {
                   state = CarState.TO_REMOVE;
            }
        } else {
            if (x == 4) {
                state = CarState.TO_REMOVE;
            }
        }
    }


//    private void disembarkation() {
//        if(nextState == CarState.DISEMBARKATION) {
//            sendLog(currentState.toString());
//            calculateSpeed(0);
//            takeNap(MIN_DISEMBARKATION_TIME, MAX_DISEMBARKATION_TIME);
//            nextState = CarState.TO_REMOVE;
//            currentState = nextState;
//        }
//    }

    private void calculateSpeed(WorldArea worldArea) {
        int distance = worldMap.getWorldZoneWidth(worldArea);
        speed = 10 * direction.getDirection() * distance/worldArea.getTimeLimit();
    }

    private void updatePosition() {
        x += speed;
    }

//    public void sendLog(String message) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<< [Car");
//        sb.append(String.format("%04d", carID));
//        sb.append(" -> ");
//        sb.append(direction);
//        sb.append("] ");
//        sb.append(message);
//        logPanel.addLog(sb.toString());
//    }

//    private void takeNap(int time) {
//        try {
//            Thread.sleep(time);
//        } catch (InterruptedException e) {
//            System.err.println("Sleep error");
//        }
//    }
//
//    private void takeNap(int minTime, int maxTime) {
//        int napTime = ThreadLocalRandom.current().nextInt(minTime, maxTime);
//        takeNap(napTime);
//    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, WIDTH, HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.DARK_GRAY);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (WIDTH - fm.stringWidth(Integer.toString(carID)))/2;
        int ty = y + (HEIGHT - fm.getHeight())/2 + fm.getAscent();
        g.drawString(Integer.toString(carID), tx, ty);
    }

    public boolean isToRemove() {
        return state == CarState.TO_REMOVE;
    }

    @Override
    public String toString() {
        return "Car " + carID;
    }

    public int getCarID() {
        return carID;
    }

    public Direction getCarDirection() {
        return direction;
    }
}