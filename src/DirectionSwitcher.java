import java.util.concurrent.ThreadLocalRandom;

public class DirectionSwitcher implements Runnable {

    private final static int SWITCH_TIME = 10000;

    private boolean useSwitcher;
    private Direction previousDirection;

    public DirectionSwitcher() {
        useSwitcher = true;
        if(ThreadLocalRandom.current().nextBoolean())
            previousDirection = Direction.EAST;
        else
            previousDirection = Direction.WEST;
    }

    @Override
    public void run() {
        while(useSwitcher) {

            Direction newDirection;
            if(previousDirection == Direction.WEST){
                newDirection = Direction.EAST;
            } else {
                newDirection = Direction.WEST;
            }

            directions.clear();
            directions.add(newDirection);
            previousDirection = newDirection;

            try {
                Thread.sleep(SWITCH_TIME);
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
        }
    }

    public void closeSwitcher() {
        this.useSwitcher = false;
    }
}