package Core;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * An algorithm should contain a main method to run it.
 * The main method should create a GDAForm and call the run method on it.
 *
 * This extends JPanel so that the user can decide how to display the algorithm.
 * The don't have to override the paint component method if they don't want to.
 */
public abstract class Algorithm extends JPanel {
    /**
     * This method is used to tell the algorithm to update the state of the algorithm
     */
    public abstract void tick();

    /**
     * Used to determine if the algorithm is complete or if there are more states to go
     */
    public abstract boolean isDone();

    /**
     * The name of the algorithm (displayed on the GUI)
     */
    public abstract String getName();
}
