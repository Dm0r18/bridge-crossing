import java.util.ArrayList;

public class Bridge {
    private static Bridge instance = null;
    private final ArrayList<Car> carCrossing;
    private Direction direction = null;
    private boolean switchDirection = false;
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
        notifyBuses();
//        setAllowedDirections();
    }

    private boolean isSafe(Car c) {
        if(direction == null) {
            return true;
        } else if(switchDirection) {
            return c.getCarDirection() != direction;
        } else return c.getCarDirection() == direction;
    }

    public synchronized void getOnTheBridge(Car car) {
        while (carCrossing.size() >= bridgeThroughput || !isSafe(car)) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
            System.out.println(car + " is waiting");
        }
        carCrossing.add(car);
        direction = car.getCarDirection();
    }

    public synchronized void getOffTheBridge(Car car) {
        switchDirection = false;
        carCrossing.remove(car);
        if(carCrossing.size() == 0) {
            switchDirection = true;
        }
        notifyBuses();
    }

    private synchronized void notifyBuses() {
//        for(int i = bridgeThroughput - carCrossing.size(); i>0; i--) {
//                notify();
//            }
        notifyAll();
    }
}
