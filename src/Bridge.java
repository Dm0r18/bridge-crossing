import java.util.ArrayList;

//enum BridgeThroughput {
//    ONE_BUS_ONE_WAY("One bus, one way", 1),
//    MANY_BUSES_ONE_WAY("Many buses, one way", 3),
//    MANY_BUSES_BOTH_WAYS("Many buses, both ways", 3),
//    UNLIMITED("Unlimited", Integer.MAX_VALUE);
//
//
//    private String text;
//    private int busLimit;
//
//    private BridgeThroughput(String text, int busLimit) {
//        this.text = text;
//        this.busLimit = busLimit;
//    }
//
//    @Override
//    public String toString() {
//        return text;
//    }
//
//    public void setbusLimit(int busLimit) {
//        this.busLimit = busLimit;
//    }
//
//    public int getBusLimit() {
//        return busLimit;
//    }
//
//}

public class Bridge {
    private static Bridge instance = null;
    private ArrayList<Car> carWaiting;
    private ArrayList<Car> carCrossing;
    private Direction direction;
    private DirectionSwitcher directionSwitcher;
    private int bridgeThroughput;

    private Bridge() {
        carWaiting = new ArrayList<>();
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
//        setAllowedDirections();
    }

//    private synchronized void setAllowedDirections() {
//        if(directionSwitcher == null) {
//            directionSwitcher = new DirectionSwitcher();
//            new Thread(directionSwitcher, "DIRECTON_SWITCHER").start();
//
//        }
//
//        notifyBuses();
//    }

    public synchronized void getOnTheBridge(Car car) {
        while(carCrossing.size() >= bridgeThroughput ||
                !direction.contains(car.getCarDirection())) {

            carWaiting.add(car);

            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }

            carWaiting.remove(car);
        }
        carCrossing.add(car);
    }

    public synchronized void getOffTheBridge(Car car) {
        carCrossing.remove(car);
        notifyBuses();
    }

    private synchronized void notifyBuses() {
        for(int i = bridgeThroughput - carCrossing.size(); i>0; i--) {
                notify();
            }
    }

    public synchronized String getWaitngBusesList() {
        StringBuilder sb = new StringBuilder();
        for (Car car : carWaiting) {
            sb.append(car.toString());
            sb.append("   ");
        }
        return sb.toString();
    }

    public synchronized String getCrossingBusesList() {
        StringBuilder sb = new StringBuilder();
        for (Car car : carCrossing) {
            sb.append(car.toString());
            sb.append("   ");
        }
        return sb.toString();
    }
}
