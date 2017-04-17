package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class SelectionSort implements Algorithm {
    public static void main(String[] args) throws Exception {
        SelectionSort s = new SelectionSort(100);

        GDAForm form = new GDAForm(s, 1000, 1000, 100);

        while (true) {
            s.randomize();
            form.run();

            Thread.sleep(1000);
        }
    }

    private enum STATE {
        SWAP, SEARCH
    }

    private int[] elements;
    private STATE state;
    private int ind;
    private int min;

    public SelectionSort(int n) {
        elements = new int[n];
    }

    public void randomize() {
        ind = 0;
        state = STATE.SEARCH;

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
        case SEARCH:
            min = ind;

            for (int i = ind + 1; i < elements.length; i++) {
                if (elements[i] < elements[min])
                    min = i;
            }

            state = STATE.SWAP;
            break;
        case SWAP:
            swap(min, ind);
            state = STATE.SEARCH;
            min = ind;
            ind++;
            break;
        }
    }

    @Override
    public boolean isDone() {
        return ind >= elements.length;
    }

    @Override
    public void display(Graphics g, int width, int height) {
        int w = (width - 20) / elements.length;
        int h = (height - 20) / elements.length;

        g.setColor(Color.BLUE);

        for (int i = 0; i < elements.length; i++) {
            g.fillRect(10 + w * i, 10, w, elements[i] * h);
        }

        g.setColor(Color.RED);

        if (min >= 0) {
            g.fillRect(10 + w * min, 10, w, elements[min] * h);
        }
    }

    @Override
    public String getName() {
        return "Selection Sort";
    }

}
