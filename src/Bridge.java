import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

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
    private HashSet<Direction> directions;
    private int bridgeThroughput;

    private Bridge() {
        directions = new HashSet<>();
        carWaiting = new ArrayList<>();
        carCrossing = new ArrayList<>();
        bridgeThroughput = 3;
    }

    public static Bridge getInstance() {
        if(instance == null) {
            instance = new Bridge();
        }
        else {
            return instance;
        }
    }

    private DirectionSwitcher directionSwitcher;

    public int getBridgeThroughput() {
        return bridgeThroughput;
    }

    public void setBridgeThroughput(BridgeThroughput bridgeThroughput) {
        this.bridgeThroughput = bridgeThroughput;
        setAllowedDirections();
    }

    private synchronized void setAllowedDirections() {


        switch (bridgeThroughput) {
            case ONE_BUS_ONE_WAY:
            case UNLIMITED:
            case MANY_BUSES_BOTH_WAYS:
                if(directionSwitcher != null) {
                    directionSwitcher.closeSwitcher();
                    directionSwitcher = null;
                }
                directions.clear();
                directions.add(Direction.EAST);
                directions.add(Direction.WEST);
                break;
            case MANY_BUSES_ONE_WAY:
                if(directionSwitcher == null) {
                    directionSwitcher = new DirectionSwitcher();
                    new Thread(directionSwitcher, "DIRECTON_SWITCHER").start();
                }
                break;
            default:
                break;
        }

        notifyBuses();
    }

    public synchronized void getOnTheBridge(Bus bus) {
        while(carCrossing.size() >= bridgeThroughput.getBusLimit() ||
                !directions.contains(bus.getBusDirection())) {

            carWaiting.add(bus);
            bus.setInactiveColor();
            bus.sendLog(BusState.GET_ON_BRIDGE.toString());

            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }

            carWaiting.remove(bus);
        }

        bus.setActiveColor();
        carCrossing.add(bus);
    }

    public synchronized void getOffTheBridge(Bus bus) {
        carCrossing.remove(bus);
        bus.sendLog(BusState.GET_OFF_BRIDGE.toString());

        notifyBuses();
    }

    private synchronized void notifyBuses() {
        switch(bridgeThroughput) {
            case MANY_BUSES_ONE_WAY:
            case MANY_BUSES_BOTH_WAYS:
                for(int i = bridgeThroughput.getBusLimit()- carCrossing.size(); i>0; i--) {
                    notify();
                }
                break;
            case ONE_BUS_ONE_WAY:
                notify();
                break;
            case UNLIMITED:
                notifyAll();
                break;
            default:
                break;
        }

    }

    public synchronized String getWaitngBusesList() {
        StringBuilder sb = new StringBuilder();
        for (Bus bus : carWaiting) {
            sb.append(bus.toString());
            sb.append("   ");
        }
        return sb.toString();
    }

    public synchronized String getCrossingBusesList() {
        StringBuilder sb = new StringBuilder();
        for (Bus bus : carCrossing) {
            sb.append(bus.toString());
            sb.append("   ");
        }
        return sb.toString();
    }

}