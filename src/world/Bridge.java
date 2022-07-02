package world;

import vehicle.Car;
import vehicle.Direction;

import java.util.ArrayList;

public class Bridge {
    private static Bridge instance = null;
    private final ArrayList<Car> carCrossing;
    private Direction direction = null;

    private int bridgeThroughput;

    private Bridge() {
        carCrossing = new ArrayList<>();
        bridgeThroughput = 3;
    }

    public static Bridge getInstance() {
        if(instance == null) {
            instance = new Bridge();
        }
        return instance;
    }

    public int getBridgeThroughput() {
        return bridgeThroughput;
    }

    public void setBridgeThroughput(int bridgeThroughput) {
        this.bridgeThroughput = bridgeThroughput;
        notifyCars();
    }

    public synchronized void getOnTheBridge(Car car) {
        while (stop(car)) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
        }
        carCrossing.add(car);
        direction = car.getCarDirection();
    }

    public synchronized void getOffTheBridge(Car car) {
        carCrossing.remove(car);
        notifyCars();
    }

    private synchronized void notifyCars() {
        for(int i = 0; i < bridgeThroughput - carCrossing.size(); i++){
            notify();
        }
    }

    private boolean stop(Car c) {
        if(carCrossing.size() >= bridgeThroughput) {
            return true;
        } else {
            if(!carCrossing.isEmpty()) {
                return c.getCarDirection() != direction;
            }
            return false;
        }
    }
}
