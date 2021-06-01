package Miguel.Industries;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Miguel Emmara - 18022146
 */
public class Machine implements Runnable {
    public static long MIN_CONSUMPTION_TIME = 100;
    public static long MAX_CONSUMPTION_TIME = 1000;
    public static long MIN_PRODUCTION_TIME = 100;
    public static long MAX_PRODUCTION_TIME = 1000;

    public static ConveyorBelt[] conveyorBelts;
    public Thread thready;
    public Random random;
    public boolean connected;

    public Machine(ConveyorBelt[] conveyorBelts) {
        setConveyorBelts(conveyorBelts);
        setThready(new Thread(this));
        getThready().start();
        setRandom(new Random());
        setConnected(false);
    }

    @Override
    public void run() {
        int i = 0;
        while (getThready().isAlive()) {
            if (isConnected() && !getConveyorBelts()[i].isFull()) {

                long sleep = getRandom().nextInt((int) (MAX_PRODUCTION_TIME - MIN_PRODUCTION_TIME)) + MIN_PRODUCTION_TIME;
                try {
                    Thread.sleep(sleep);
                } catch (Exception ignored) {}
                getConveyorBelts()[i].postParcel(newParcel(), this);
            } else if (isConnected() && getConveyorBelts()[i].isFull()) {
                getConveyorBelts()[i].disconnectMachine(this);
                setConnected(false);
            }
            if (!isConnected()) {

                for (i = 0; i < getConveyorBelts().length; i++) {
                    if (!getConveyorBelts()[i].isFull()) {
                        try {
                            if (getConveyorBelts()[i].connectMachine(this)) {
                               setConnected(true);
                                break;
                            }
                        } catch (Exception ignored) {}
                    }
                }

            }
        }

        /*final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        final Runnable producer = () -> {
            for (int j = 0; j < 1000; j++) {
                try {
                    System.out.println("Producing: " + j);
                    queue.put(j);

                    //Adjust production speed by modifying the sleep time
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //someone signaled us to terminate
                    break;
                }
            }
        };

        final Thread producerThread = new Thread(producer);
        producerThread.start();

        try {
            producerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public Parcel<?> newParcel() {
        // Random Letter
        char c = (char) (random.nextInt(26) + 'A');

        // Get #RGB Colour
        Color colour = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());

        int consumeTime = (int) (random.nextInt((int) MAX_CONSUMPTION_TIME) + MIN_CONSUMPTION_TIME);
        int priority = random.nextInt(3) + 1;
        return new Parcel<>(c, colour, consumeTime, priority);
    }

    public void drawMachine(Graphics g, int x, int y, int radius) {
        g.setColor(Color.RED);
        g.fillOval(x, y, radius, radius);
    }

    // GETTERS & SETTERS
    public static ConveyorBelt[] getConveyorBelts() {
        return conveyorBelts;
    }

    public static void setConveyorBelts(ConveyorBelt[] conveyorBelts) {
        Machine.conveyorBelts = conveyorBelts;
    }

    public Thread getThready() {
        return thready;
    }

    public void setThready(Thread thready) {
        this.thready = thready;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
