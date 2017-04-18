package Core;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GDAForm {
    private Algorithm algo;
    private AlgoPanel display;
    private long sleep;

    public GDAForm(Algorithm a, int w, int h, long s) {
        algo = a;
        sleep = s;

        JFrame frame = new JFrame(a.getName());
        frame.setSize(w, h);

        display = new AlgoPanel();
        frame.add(display);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void update() {
        display.repaint();
    }

    public void run() {
        while (!algo.isDone()) {
            algo.tick();
            update();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
        }
    }

    /*
     * Theese are to allow for the same form, but change what the algorithm is.
     */
    public void setAlgorithm(Algorithm a) {
        algo = a;
    }

    /*
     * End algorithm methods
     */

    private class AlgoPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            int width = this.getWidth();
            int height = this.getHeight();

            g.clearRect(0, 0, width, height);

            try {
                algo.display(g, width, height);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
