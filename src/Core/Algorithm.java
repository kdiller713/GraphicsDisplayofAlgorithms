package Core;

import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class Algorithm extends JPanel {
    /**
     * This method is used to tell the algorithm to update to the next state
     */
    public abstract void tick();

    /**
     * Used to determine if the algorithm is finished or not
     */
    public abstract boolean isDone();

    /**
     * The name to display on the GUI
     */
    public abstract String getName();
}
