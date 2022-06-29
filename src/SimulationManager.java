
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTextField;

public class SimulationManager implements Runnable {

    private static int BUS_ID = 1;

    private LogPanel logPanel;
    private DrawPanel drawPanel;

    private ArrayList<Car> cars;
    private Bridge bridge;
    private WorldMap worldMap;

    private JTextField busesWaitingTextField;
    private JTextField busesCrossingTextField;

    private int busSpawnMaxDelay;

    public SimulationManager(LogPanel logPanel, DrawPanel drawPanel, int busSpawnMaxDelay, JTextField busesInQueueTextField, JTextField busesOnBridgeTextField) {
        this.logPanel = logPanel;
        this.drawPanel = drawPanel;
        this.busSpawnMaxDelay = busSpawnMaxDelay;
        busesWaitingTextField = busesInQueueTextField;
        busesCrossingTextField = busesOnBridgeTextField;
        bridge = Bridge.getInstance();
        cars = new ArrayList<Car>();
    }

    public float getBusSpawnMaxDelay (){
        return busSpawnMaxDelay;
    }

    public void setMaxBusSpawnRate(int busSpawnMaxDelay) {
        this.busSpawnMaxDelay = busSpawnMaxDelay;
    }

    public void setBridgeThroughput(BridgeThroughput bridgeThroughput) {
        bridge.setBridgeThroughput(bridgeThroughput);
    }

    @Override
    public void run() {
        worldMap = new WorldMap(drawPanel.getSize());
        drawPanel.setSimulationManager(this);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    Car car = new Car(bridge, logPanel, worldMap);
                    synchronized (cars) {
                        cars.add(car);
                    }
                    new Thread(car, "BUS" + BUS_ID++).start();

                    try {
                        int timeToNextSpawn = ThreadLocalRandom.current().nextInt((int)busSpawnMaxDelay/2, busSpawnMaxDelay);
                        Thread.sleep(timeToNextSpawn);
                    } catch (InterruptedException e) {
                        System.err.println("Sleep error");
                    }
                }
            }
        }, "BUS_SPAWNER").start();

        while(true) {
            synchronized (cars) {
                cars.removeIf((x) -> x.isToRemove());
            }

            busesWaitingTextField.setText(bridge.getWaitngBusesList());
            busesCrossingTextField.setText(bridge.getCrossingBusesList());

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