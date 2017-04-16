package Core;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GDAForm {
    private Algorithm algo;
    private AlgoPanel display;

    public GDAForm(Algorithm a, int w, int h) {
        algo = a;
        
        JFrame frame = new JFrame(a.getName());
        frame.setSize(w ,h);
        
        display = new AlgoPanel();
        frame.add(display);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void update() {
        display.repaint();
    }
    
    /*
     * Theese are to allow for the same form, but change what the algorithm is.
     */
    public Algorithm getAlgorithm(){
        return algo;
    }
    
    public void setAlgorithm(Algorithm a){
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
            algo.display(g, width, height);
        }
    }
}
