package Beetles;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class Original2D extends Algorithm {
    public static void main(String[] args) throws Exception {
        Original2D df = new Original2D(1000);
        GDAForm form = new GDAForm(df, 1000, 1000, 50);
        form.run();
    }
    
    private Beetle[] beetles;
    
    public Original2D(int numBeetles){
        beetles = new Beetle[numBeetles];
        
        for(int i = 0; i < numBeetles; i++){
            beetles[i] = new Beetle(Math.random(), Math.random());
        }
        
        for(int i = 0; i < numBeetles; i++){
            beetles[i].following.add(beetles[(i+1) % numBeetles]);
        }
        
        adjust();
    }
    
    @Override
    public void tick() {
        for(Beetle b : beetles){
            b.tick();
        }
        
        adjust();
    }
    
    private void adjust(){
        double xMin = 10;
        double xMax = -10;
        double yMin = 10;
        double yMax = -10;
            
        for(Beetle b : beetles){
            xMin = Math.min(xMin, b.x);
            xMax = Math.max(xMax, b.x);
            yMin = Math.min(yMin, b.y);
            yMax = Math.max(yMax, b.y);
        }
        
        // Figures out the arguments for the adjustment
        double xMid = (xMin + xMax) / 2;
        double yMid = (yMin + yMax) / 2;
        
        double xScale = 0.5 / (xMax - xMid);
        double yScale = 0.5 / (yMax - yMid);
        
        for(Beetle b : beetles){
            b.scale(xMid, xScale, yMid, yScale);
        }
    }

    @Override
    public boolean isDone() {
        return false; // This is never done
    }

    @Override
    public String getName() {
        return "Beetles (1 Partner)";
    }
    
    @Override
    public void paintComponent(Graphics g){
        int width = this.getWidth();
        int height = this.getHeight();
        g.clearRect(0, 0, width, height);
 
        for(Beetle b : beetles){
            b.draw(g, width, height);
        }
    }
}
