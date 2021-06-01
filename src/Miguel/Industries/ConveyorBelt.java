package Miguel.Industries;

import java.awt.*;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author Miguel Emmara - 18022146
 */
public class ConveyorBelt {
    private static final int DEFAULT_CAPACITY = 10;
    private int maxCapacity;
    private Machine connectedMachine;
    private Dispatcher connectedDispatcher;
    private PriorityQueue<Parcel<?>> queue;

    public ConveyorBelt(int maxCapacity) {
        setMaxCapacity(maxCapacity);
        setConnectedMachine(null);
        setConnectedDispatcher(null);
        setQueue(new PriorityQueue<>(maxCapacity));
    }

    public ConveyorBelt() {
        this(DEFAULT_CAPACITY);
    }

    public synchronized boolean connectMachine(Machine machine) {
        if (getConnectedMachine() == null) {
            setConnectedMachine(machine);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean connectDispatcher(Dispatcher dispatcher) {
        if (getConnectedDispatcher() == null) {
            setConnectedDispatcher(dispatcher);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean disconnectMachine(Machine machine) {
        if (getConnectedMachine() == machine) {
            setConnectedMachine(null);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean disconnectDispatcher(Dispatcher dispatcher) {
        if (getConnectedDispatcher() == dispatcher) {
            setConnectedDispatcher(null);
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return getMaxCapacity();
    }

    public boolean isEmpty() {
        return getQueue().isEmpty();
    }

    public boolean isFull() {
        return getQueue().size() == getMaxCapacity();
    }

    public synchronized boolean postParcel(Parcel<?> p, Machine machine) {
        if (getConnectedMachine() == machine) {
            getQueue().offer(p);
            return true;
        } else {
            return false;
        }
    }

    public synchronized Parcel<?> getFirstParcel(Dispatcher dispatcher) {
        if (getConnectedDispatcher() == dispatcher) {
            return getQueue().peek();
        } else
            return null;
    }

    public synchronized Parcel<?> retrieveParcel(Dispatcher dispatcher) {
        if (getConnectedDispatcher() == dispatcher) {
            return getQueue().poll();
        } else
            return null;
    }

    public void drawBelt(Graphics g, int x, int y, int width, int height) {
        int size = width / getMaxCapacity();

        g.setColor(Color.black);
        g.drawRect(x, y, width, height);

        for (int i = 1; i < getMaxCapacity(); i++) {
            g.drawLine(x + i * size, y, x + i * size, y + height);
        }

        Parcel<?>[] parcels = new Parcel<?>[getQueue().size()];
        parcels = (getQueue().toArray(parcels));
        Arrays.sort(parcels);

        for (int i = 0; i < getQueue().size(); i++) {
            parcels[i].drawBox(g, x + i * size, y, size, height);
        }

        if (getConnectedDispatcher() != null) {
            getConnectedDispatcher().drawDispatcher(g, x + (-2) - height, y, height);
        }

        if (getConnectedMachine() != null) {
            getConnectedMachine().drawMachine(g, x + width + 2, y, height);
        }
    }

    // GETTERS & SETTERS
    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Machine getConnectedMachine() {
        return connectedMachine;
    }

    public void setConnectedMachine(Machine connectedMachine) {
        this.connectedMachine = connectedMachine;
    }

    public Dispatcher getConnectedDispatcher() {
        return connectedDispatcher;
    }

    public void setConnectedDispatcher(Dispatcher connectedDispatcher) {
        this.connectedDispatcher = connectedDispatcher;
    }

    public PriorityQueue<Parcel<?>> getQueue() {
        return queue;
    }

    public void setQueue(PriorityQueue<Parcel<?>> queue) {
        this.queue = queue;
    }
}
