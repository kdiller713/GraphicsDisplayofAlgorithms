package Beetles;

import java.util.LinkedList;

import java.awt.Graphics;
import java.awt.Color;

public class Beetle {
    public double xp, yp;
    public double x, y;
    public LinkedList<Beetle> following;
    
    private Color c;
    
    private static final double PERCENT_DIST = 0.5;
    private static final int RAD = 5;
    
    public Beetle(double x0, double y0){
        x = xp = x0;
        y = yp = y0;
        
        following = new LinkedList<Beetle>();
        c = new Color((int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
    }
    
    // Move the beetle towards the beetles it is following
    public void tick(){
        double scale = 1.0 / following.size();
        double xDelta = 0;
        double yDelta = 0;
        
        for(Beetle other : following){
            xDelta += (other.xp - xp) * scale;
            yDelta += (other.yp - yp) * scale;
        }
        
        x = xp + xDelta * PERCENT_DIST;
        y = yp + yDelta * PERCENT_DIST;
    }
    
    // Adjust the points to draw properly
    // Should be between 0 and 1
    public void scale(double xMid, double xScale, double yMid, double yScale){
        xp = (x - xMid) * xScale + 0.5;
        yp = (y - yMid) * yScale + 0.5;
    }
    
    public void draw(Graphics g, int width, int height){
        g.setColor(c);
    
        width -= 2 * RAD;
        height -= 2 * RAD;
        g.fillOval((int)(xp * width), (int)(yp * height), 2 * RAD, 2 * RAD);
    }
}
