package Core;

import java.awt.Graphics;

public interface Algorithm {
    public void tick();

    public boolean isDone();

    public void display(Graphics g, int width, int height);

    public String getName();
}
