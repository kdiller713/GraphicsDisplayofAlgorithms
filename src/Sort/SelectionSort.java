package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class SelectionSort extends Algorithm {
    public static void main(String[] args) throws Exception {
        SelectionSort s = new SelectionSort(100);

        GDAForm form = new GDAForm(s, 1000, 1000, 10);

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
    private int loc;

    public SelectionSort(int n) {
        elements = new int[n];
    }

    public void randomize() {
        ind = 0;
        min = 0;
        loc = ind + 1;
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
            if(loc < elements.length){
                if(elements[loc] < elements[min])
                    min = loc;
                
                loc++;
            }else{
                state = STATE.SWAP;
            }
            break;
        case SWAP:
            swap(min, ind);
            state = STATE.SEARCH;
            ind++;
            min = ind;
            loc = min + 1;
            break;
        }
    }

    @Override
    public boolean isDone() {
        return ind >= elements.length;
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
        if(loc >= 0 && loc < elements.length){
            g.fillRect(10 + w * loc, 10, w, elements[loc] * h);
        }

        if (min >= 0 && min < elements.length) {
            g.setColor(Color.green);
            g.fillRect(10 + w * min, 10, w, elements[min] * h);
        }
    }

    @Override
    public String getName() {
        return "Selection Sort";
    }

}
