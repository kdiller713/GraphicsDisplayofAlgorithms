package Core;

import java.awt.Graphics;

public interface Algorithm {
    public void tick();

    public boolean isDone();

    public void display(Graphics g);

    public String getName();
}
