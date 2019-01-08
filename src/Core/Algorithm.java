package Core;

import java.awt.Graphics;

public interface Algorithm {
    /**
     * This method is used to tell the algorithm to update to the next state
     */
    public void tick();

    /**
     * Used to determine if the algorithm is finished or not
     */
    public boolean isDone();

    /**
     * Updates the graphics of the GUI to show the new state
     */
    public void display(Graphics g, int width, int height);

    /**
     * The name to display on the GUI
     */
    public String getName();
}
