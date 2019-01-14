package Core;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates a GUI for the algorithm to be displayed on.
 */
public class GDAForm {
    private Algorithm algo;
    private long sleep;

    /**
     * Creates the GDA form with an algorithm, the dimensions of the GUI, and the time between iterations
     * for the algorithm.
     */
    public GDAForm(Algorithm a, int w, int h, long s) {
        algo = a;
        sleep = s;

        JFrame frame = new JFrame(a.getName());
        frame.setSize(w, h);

        frame.add(algo);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Starts running the algorithm and updating the UI as it goes.
     */
    public void run() {
        while (!algo.isDone()) {
            try {
                algo.tick();
                algo.repaint();
                Thread.sleep(sleep);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
