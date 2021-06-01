package Miguel.Industries;

import java.awt.*;

/**
 * @author Miguel Emmara - 18022146
 */
public class Dispatcher implements Runnable {
    private ConveyorBelt[] conveyorBelts;
    private Thread thready;
    private boolean connected;

    public Dispatcher(ConveyorBelt[] conveyorBelts) {
        setConveyorBelts(conveyorBelts);
        setThready(new Thread(this));
        getThready().start();
        setConnected(false);
    }

    @Override
    public void run() {
        int i = 0;
        while (getThready().isAlive()) {
            if (isConnected()) {
                getConveyorBelts()[i].getFirstParcel(this).consume();
                getConveyorBelts()[i].retrieveParcel(this).consume();
                getConveyorBelts()[i].disconnectDispatcher(this);
                setConnected(false);
                i = (i + 1) % getConveyorBelts().length;
            }
            if (!isConnected()) {
                while (i < getConveyorBelts().length) {
                    if (!getConveyorBelts()[i].isEmpty()) {
                        try {
                            if (getConveyorBelts()[i].connectDispatcher(this)) {
                                setConnected(true);
                                break;
                            }
                        } catch (Exception ignored) {}
                    }
                    i = (i + 1) % getConveyorBelts().length;
                }

            }
        }
    }

    // GETTERS & SETTERS
    public ConveyorBelt[] getConveyorBelts() {
        return conveyorBelts;
    }

    public void setConveyorBelts(ConveyorBelt[] conveyorBelts) {
        this.conveyorBelts = conveyorBelts;
    }

    public Thread getThready() {
        return thready;
    }

    public void setThready(Thread thready) {
        this.thready = thready;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void drawDispatcher(Graphics g, int x, int y, int radius) {
        g.setColor(Color.BLUE);
        g.fillOval(x, y, radius, radius);
    }
}
