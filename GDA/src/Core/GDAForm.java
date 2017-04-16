package Core;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GDAForm {
    private Algorithm algo;
    private AlgoPanel display;

    public GDAForm(Algorithm a) {
        algo = a;
        
        // TODO Auto-generated constructor stub
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
