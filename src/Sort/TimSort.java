package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;

public class TimSort extends Algorithm {
    public static void main(String[] args) throws Exception {
        TimSort s = new TimSort(100);

        GDAForm form = new GDAForm(s, 1000, 1000, 10);

        while (true) {
            s.randomize();
            form.run();

            Thread.sleep(1000);
        }
    }

    private enum STATE {
        SEARCH, MERGE, CHECK
    }

    private int[] elements;
    private STATE state;

    private int i, j, k;
    private boolean lower, done;

    public TimSort(int n) {
        elements = new int[n];
    }

    public void randomize() {
        state = STATE.SEARCH;
        done = false;
        lower = false;
        i = 0;
        j = 1;
        k = 0;

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
        case CHECK:
            if (!lower && i == 0) {
                done = true;
                i = -1;
                j = -1;
                k = -1;
            } else {
                lower = false;
                i = 0;
                j = 1;
                k = 0;
                state = STATE.SEARCH;
            }
            break;
        case MERGE:
            int dist = k - i;
            int[] temp = new int[dist];

            int a = i;
            int b = j;
            int c = 0;

            while (a < j && b < k) {
                if (elements[a] < elements[b]) {
                    temp[c] = elements[a];
                    a++;
                } else {
                    temp[c] = elements[b];
                    b++;
                }

                c++;
            }

            while (a < j) {
                temp[c] = elements[a];
                a++;
                c++;
            }

            while (b < k) {
                temp[c] = elements[b];
                b++;
                c++;
            }

            for (a = 0; a < temp.length; a++) {
                elements[a + i] = temp[a];
            }

            i = k;
            k = 0;
            j = i + 1;
            state = STATE.SEARCH;
            lower = false;
            break;
        case SEARCH:
            if (j >= elements.length || k >= elements.length) {
                if (lower)
                    state = STATE.MERGE;
                else
                    state = STATE.CHECK;

                break;
            }

            if (!lower && elements[j] >= elements[j - 1]) {
                j++;
            } else if (!lower) {
                lower = true;
                k = j + 1;
            } else if (elements[k] >= elements[k - 1]) {
                k++;
            } else {
                state = STATE.MERGE;
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

        if (i >= 0 && j >= 0 && i < elements.length && j < elements.length) {
            g.fillRect(10 + w * i, 10, w, elements[i] * h);
            g.fillRect(10 + w * j, 10, w, elements[j] * h);
        }

        if (lower && k < elements.length) {
            g.fillRect(10 + w * k, 10, w, elements[k] * h);
        }
    }

    @Override
    public String getName() {
        return "Tim Sort";
    }
}
