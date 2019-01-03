package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;
import LinkedLibraries.Lists.KInvalidException;
import LinkedLibraries.Lists.KStack;

public class QuickSort implements Algorithm {
    public static void main(String[] args) throws Exception {
        QuickSort s = new QuickSort(100);

        GDAForm form = new GDAForm(s, 1000, 1000, 10);

        while (true) {
            s.randomize();
            form.run();

            Thread.sleep(1000);
        }
    }

    private enum STATE {
        DIVE_LEFT, DIVE_RIGHT, SEARCH, DONE, SWAP
    }

    private class StackElements {
        public STATE state;
        public int min, max, pivot;

        public int i, j;

        public StackElements(STATE s, int a, int b) {
            state = s;
            min = a;
            max = b;
            pivot = (a + b) / 2;
            
            i = Math.min(a, elements.length - 1);
            j = Math.max(0, b);
        }
    }

    private int[] elements;
    private KStack<StackElements> stack;

    public QuickSort(int n) {
        elements = new int[n];
        stack = new KStack<StackElements>();
    }

    public void randomize() {
        stack = new KStack<StackElements>();
        StackElements start = new StackElements(STATE.SEARCH, 0,
                elements.length - 1);
        stack.push(start);

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
        StackElements top;
        try {
            top = stack.peek();

            if (top.min >= top.max) {
                top.state = STATE.DONE;
            }

            switch (top.state) {
            case DIVE_LEFT:
                top.state = STATE.DIVE_RIGHT;

                int a = top.min;
                int b = top.pivot - 1;
                stack.push(new StackElements(STATE.SEARCH, a, b));
                break;
            case DIVE_RIGHT:
                top.state = STATE.DONE;

                a = top.pivot + 1;
                b = top.max;
                stack.push(new StackElements(STATE.SEARCH, a, b));
                break;
            case DONE:
                stack.pop();
                break;
            case SEARCH:
                if (top.i >= top.j) {
                    top.state = STATE.SWAP;
                    break;
                }
                if (elements[top.i] <= elements[top.pivot]) {
                    top.i++;
                } else if (elements[top.j] >= elements[top.pivot]) {
                    top.j--;
                } else {
                    swap(top.i, top.j);
                }
                break;
            case SWAP:
                if (top.i < top.pivot || elements[top.i] < elements[top.pivot]) {
                    swap(top.i, top.pivot);
                    top.pivot = top.i;
                }else{
                    swap(top.i - 1, top.pivot);
                    top.pivot = top.i - 1;  
                }
                top.state = STATE.DIVE_LEFT;
                break;
            }
        } catch (KInvalidException e) {
        }
    }

    @Override
    public boolean isDone() {
        return stack.length() == 0;
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

        try {
            StackElements top = stack.peek();
            g.fillRect(10 + w * top.i, 10, w, elements[top.i] * h);
            g.fillRect(10 + w * top.j, 10, w, elements[top.j] * h);

            g.setColor(Color.green);
            g.fillRect(10 + w * top.pivot, 10, w, elements[top.pivot] * h);

            g.setColor(Color.BLACK);
            g.drawLine(10, 10 + elements[top.pivot] * h, width - 10,
                    elements[top.pivot] * h + 10);
        } catch (KInvalidException e) {
        }
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

}
