package Miguel.Industries;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Miguel Emmara - 18022146
 */
public class Parcel<E> implements Comparable<Parcel<?>> {
    private final E element;
    private final Color colour;
    private final int consumeTime;
    private final int priority;
    private final long timestamp;

    public Parcel(E element, Color colour, int consumeTime, int priority) {
        this.element = element;
        this.colour = colour;
        this.consumeTime = consumeTime;
        this.priority = priority;
        this.timestamp = System.nanoTime();
    }

    public void consume() {
        try {
            System.out.println("Consumed");
            Thread.sleep(getConsumeTime());
        } catch (InterruptedException ex) {
            Logger.getLogger(Parcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return getElement() + "(" + getPriority() + ")";
    }

    public void drawBox(Graphics g, int x, int y, int width, int height) {
        g.setColor(getColour());
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(getElement() + "(" + getPriority() + ")", x + width / 2, y + height / 2);

    }

    @Override
    public int compareTo(Parcel<?> p) {
        if (this.getPriority() > p.getPriority())
            return - 1;

        else if (this.getPriority() < p.getPriority())
            return 1;

         else
            return Long.compare(p.getTimestamp(), this.getTimestamp());
    }

    // Getters
    public E getElement() {
        return element;
    }

    public Color getColour() {
        return colour;
    }

    public int getConsumeTime() {
        return consumeTime;
    }

    public int getPriority() {
        return priority;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
