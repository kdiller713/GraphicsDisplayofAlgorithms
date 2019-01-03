package Cycle;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class Period implements Algorithm {
    public static void main(String[] args) throws Exception {
        long n = 101 * 103;
        Period df = new Period(n, n - 2);
        GDAForm form = new GDAForm(df, 1000, 1000, 1000);
        form.run();
    }

    private long n, a;
    private long[] vals;
    private int i;

    public Period(long n, long a) {
        this.n = n;
        this.a = a;
        vals = new long[(int) n];
        i = 1;
        vals[0] = 1;
    }

    @Override
    public void tick() {
        if (vals[i] == -1)
            return;

        vals[i] = (vals[i - 1] * a) % n;

        if (vals[i] == 1) {
            vals[i + 1] = -1;
            System.out.println(i);
        }

        i++;
    }

    @Override
    public boolean isDone() {
        return vals[i] == -1;
    }

    @Override
    public void display(Graphics g, int width, int height) {
        width = width - 10;
        height = height - 10;

        double radsPer = 2 * Math.PI / n;

        int i = 0;

        g.setColor(Color.red);

        int sX = width / 2 + 5;
        int sY = height / 2 + 5;

        while (vals[i + 1] > 0) {
            double rads1 = vals[i] * radsPer;
            double rads2 = vals[i + 1] * radsPer;

            double x1 = width / 2 * Math.cos(rads1) + sX;
            double y1 = height / 2 * Math.sin(rads1) + sY;

            double x2 = width / 2 * Math.cos(rads2) + sX;
            double y2 = height / 2 * Math.sin(rads2) + sY;

            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            i++;
        }

        g.setColor(Color.blue);
        g.drawOval(5, 5, width, height);
    }

    @Override
    public String getName() {
        return "Period Function";
    }

}
