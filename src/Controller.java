import vehicle.Car;
import world.Bridge;
import world.WorldMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements Runnable {
    private static final int SPAWN_DELAY = 2000;
    private final Bridge bridge = Bridge.getInstance();
    private final ArrayList<Car> cars;
    private WorldMap worldMap;
    private final DrawPanel drawPanel;

    public Controller(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
        cars = new ArrayList<>();
    }

    public int getBridgeThroughput() {
        return bridge.getBridgeThroughput();
    }

    public void setBridgeThroughput(int bridgeThroughput) {
        bridge.setBridgeThroughput(bridgeThroughput);
    }

    @Override
    public void run() {
        worldMap = WorldMap.setDimension(drawPanel.getSize());
        drawPanel.setSimulationManager(this);
        new Thread(() -> {
            while(true) {
                Car car = new Car(worldMap);
                synchronized (cars) {
                    cars.add(car);
                }
                new Thread(car, car.toString()).start();

                try {
                    int timeToNextSpawn = ThreadLocalRandom.current().nextInt(SPAWN_DELAY /2, SPAWN_DELAY);
                    Thread.sleep(timeToNextSpawn);
                } catch (InterruptedException e) {
                    System.err.println("Sleep error");
                }
            }
        }, "CAR_SPAWNER").start();

        while(true) {
            synchronized (cars) {
                cars.removeIf(Car::isToRemove);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
        }
    }

    public void draw(Graphics g) {
        if(worldMap != null) {
            worldMap.draw(g);
        }
        synchronized (cars) {
            for(Car car : cars) {
                car.draw(g);
            }
        }
    }
}