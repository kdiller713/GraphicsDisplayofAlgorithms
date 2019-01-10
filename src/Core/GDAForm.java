package Core;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GDAForm {
    private Algorithm algo;
    private long sleep;

    public GDAForm(Algorithm a, int w, int h, long s) {
        algo = a;
        sleep = s;

        JFrame frame = new JFrame(a.getName());
        frame.setSize(w, h);

        frame.add(algo);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void update() {
        algo.repaint();
    }

    public void run() {
        while (!algo.isDone()) {
            try {
                algo.tick();
                update();
                Thread.sleep(sleep);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /*
     * Theese are to allow for the same form, but change what the algorithm is.
     */
    public void setAlgorithm(Algorithm a) {
        algo = a;
    }
}
