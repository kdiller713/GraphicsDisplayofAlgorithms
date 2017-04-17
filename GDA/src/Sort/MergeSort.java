package Sort;

import java.awt.Color;
import java.awt.Graphics;

import Core.Algorithm;
import Core.GDAForm;
import LinkedLibraries.Lists.KInvalidException;
import LinkedLibraries.Lists.KStack;

public class MergeSort implements Algorithm {
    public static void main(String[] args) throws Exception {
        MergeSort s = new MergeSort(100);

        GDAForm form = new GDAForm(s, 1000, 1000, 10);

        while (true) {
            s.randomize();
            form.run();

            Thread.sleep(1000);
        }
    }

    private enum STATE {
        DIVE_LEFT, DIVE_RIGHT, MERGE, DONE
    }

    private class StackElements {
        public STATE state;
        public int min, max;

        public StackElements(STATE s, int a, int b) {
            state = s;
            min = a;
            max = b;
        }
    }

    private int[] elements;
    private KStack<StackElements> stack;

    public MergeSort(int n) {
        elements = new int[n];
        stack = new KStack<StackElements>();
    }

    public void randomize() {
        stack = new KStack<StackElements>();
        StackElements start = new StackElements(STATE.DIVE_LEFT, 0,
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
            int mid = (top.min + top.max) / 2;

            if (top.min == top.max) {
                top.state = STATE.DONE;
            }

            switch (top.state) {
            case DIVE_RIGHT:
                top.state = STATE.MERGE;
                StackElements seR = new StackElements(STATE.DIVE_LEFT, mid + 1,
                        top.max);
                stack.push(seR);
                break;
            case DIVE_LEFT:
                top.state = STATE.DIVE_RIGHT;
                StackElements seL = new StackElements(STATE.DIVE_LEFT, top.min,
                        mid);
                stack.push(seL);
                break;
            case DONE:
                stack.pop();
                break;
            case MERGE:
                int i = top.min;
                int j = mid + 1;
                int[] temp = new int[top.max - top.min + 1];
                int k = 0;

                while (i <= mid && j <= top.max) {
                    if (elements[i] < elements[j]) {
                        temp[k] = elements[i];
                        i++;
                    } else {
                        temp[k] = elements[j];
                        j++;
                    }
                    k++;
                }
                while (i <= mid) {
                    temp[k] = elements[i];
                    i++;
                    k++;
                }
                while (j <= top.max) {
                    temp[k] = elements[j];
                    j++;
                    k++;
                }

                for (int l = 0; l < temp.length; l++) {
                    elements[top.min + l] = temp[l];
                }

                top.state = STATE.DONE;
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
            g.fillRect(10 + w * top.min, 10, w, elements[top.min] * h);
            g.fillRect(10 + w * top.max, 10, w, elements[top.max] * h);

            g.setColor(Color.green);
            int mid = (top.min + top.max) / 2;
            g.fillRect(10 + w * mid, 10, w, elements[mid] * h);
        } catch (KInvalidException e) {
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

}
