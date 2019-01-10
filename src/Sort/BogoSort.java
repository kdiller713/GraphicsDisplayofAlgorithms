package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class BogoSort extends Algorithm {
    public static void main(String[] args) throws Exception {
        BogoSort s = new BogoSort(10);

        GDAForm form = new GDAForm(s, 1000, 1000, 10);

        while (true) {
            s.randomize();
            form.run();

            Thread.sleep(1000);
            break;
        }
    }

    private enum STATE {
        MOVE, CHECK
    }

    private int[] elements;
    private STATE state;
    private int ind;
    private boolean done;

    public BogoSort(int n) {
        elements = new int[n];
    }

    public void randomize() {
        ind = 0;
        state = STATE.MOVE;
        done = false;

        for (int i = 0; i < elements.length; i++) {
            elements[i] = i + 1;
        }

        for (int i = 0; i < elements.length; i++) {
            int j = (int) (Math.random() * elements.length);
            swap(i, j);
        }
    }

    private void swap(int i, int j) {
        int a = elements[i];
        elements[i] = elements[j];
        elements[j] = a;
    }

    @Override
    public void tick() {
        switch (state) {
        case MOVE:
            int j = (int) (Math.random() * elements.length);
            swap(ind, j);
            ind++;

            if (ind >= elements.length) {
                ind = 0;
                state = STATE.CHECK;
            }
            break;
        case CHECK:
            if (elements[ind] > elements[ind + 1]) {
                state = STATE.MOVE;
                ind = 0;
            } else {
                ind++;
                if (ind >= elements.length - 1)
                    done = true;
            }
            break;
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        g.clearRect(0, 0, width, height);
        
        int w = (width - 20) / elements.length;
        int h = (height - 20) / elements.length;

        g.setColor(Color.BLUE);

        for (int i = 0; i < elements.length; i++) {
            g.fillRect(10 + w * i, 10, w, elements[i] * h);
        }

        g.setColor(Color.RED);

        if (ind >= 0 && ind < elements.length) {
            g.fillRect(10 + w * ind, 10, w, elements[ind] * h);
        }
    }

    @Override
    public String getName() {
        return "Bogo Sort";
    }

}
