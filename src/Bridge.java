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
        notifyCars();
    }

    public synchronized void getOnTheBridge(Car car) {
        while (stop(car)) {
            try {
                System.out.println(car + " is waiting");
                wait();
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
        }
        while(switchDirection) {
            if(car.getCarDirection() != direction) {
                switchDirection = false;
            } else {
//                if()
                try {
                    System.out.println(car + " is waiting");
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Sleep error");
                }
            }
        }
        System.out.println(Thread.activeCount());
        carCrossing.add(car);
        direction = car.getCarDirection();
    }

    public synchronized void getOffTheBridge(Car car) {
        carCrossing.remove(car);
        if(carCrossing.size() == 0) {
            switchDirection = true;
        }
        notifyCars();
    }

    private synchronized void notifyCars() {
        for(int i = bridgeThroughput - carCrossing.size(); i>0; i--) {
                notify();
            }
//        notifyAll();
    }

    private boolean stop(Car c) {
        if(carCrossing.size() >= bridgeThroughput) {
            return true;
        } if(direction == null) {
            return false;
        } else if(switchDirection) {
            switchDirection = false;
            return c.getCarDirection().getDirection() == direction.getDirection();
        } else return c.getCarDirection() != direction;
    }
}
