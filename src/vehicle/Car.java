package vehicle;

import world.Bridge;
import world.WorldArea;
import world.WorldMap;

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
            color = new Color(188,143,143);
         } else {
            direction = Direction.WEST;
            x = worldMap.getWidth() - WIDTH - 4;
            color =  new Color(77,76,125);
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

    private void onRoadToBridge() {
        if(direction == Direction.EAST) {
            calculateSpeed(WorldArea.WEST_ROAD);
            if(worldMap.getWorldArea(x+WIDTH) == WorldArea.BRIDGE) {
                speed = 0;
                state = CarState.GET_ON_BRIDGE;
            }
        } else {
            calculateSpeed(WorldArea.EAST_ROAD);
            if(worldMap.getWorldArea(x) == WorldArea.BRIDGE) {
                speed = 0;
                state = CarState.GET_ON_BRIDGE;
            }
        }
    }

    private void getOnBridge() {
        bridge.getOnTheBridge(this);
        calculateSpeed(WorldArea.BRIDGE);
        state = CarState.CROSS_THE_BRIDGE;
    }

    private void crossTheBridge() {
        if(direction == Direction.EAST) {
            if(worldMap.getWorldArea(x) == WorldArea.EAST_GATE) {
                calculateSpeed(WorldArea.EAST_ROAD);
                state = CarState.GET_OFF_BRIDGE;
            }
        } else {
            if(worldMap.getWorldArea(x+WIDTH) == WorldArea.WEST_GATE) {
                calculateSpeed(WorldArea.WEST_ROAD);
                state = CarState.GET_OFF_BRIDGE;
            }
        }
    }

    private void getOffBridge() {
        bridge.getOffTheBridge(this);
        state = CarState.ON_ROAD_TO_OUT;
    }

    private void onRoadToParking() {
            toRemove();
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

    private void calculateSpeed(WorldArea worldArea) {
        int distance = worldMap.getWorldZoneWidth(worldArea);
        speed = 10 * direction.getDirection() * distance/worldArea.getTimeLimit();
    }

    private void updatePosition() {
        x += speed;
    }

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
        return "vehicle.Car " + carID;
    }

    public int getCarID() {
        return carID;
    }

    public Direction getCarDirection() {
        return direction;
    }
}