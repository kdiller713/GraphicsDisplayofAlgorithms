package Graphs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import Core.Algorithm;
import Core.GDAForm;
import LinkedLibraries.Lists.KInvalidException;
import LinkedLibraries.Lists.KStack;

public class BreadthFirst implements Algorithm {
    public static void main(String[] args) throws Exception {
        BreadthFirst df = new BreadthFirst(100);
        GDAForm form = new GDAForm(df, 1000, 1000, 10);

        while (true) {
            df.generate();
            form.run();

            Thread.sleep(1000);
        }
    }

    private enum STATE {
        FIND, TRACE
    }

    private boolean[][] horizontal;
    private boolean[][] vertical;
    private int size;

    private boolean done;
    private int[][] visited;
    private KStack<Point> path;
    private int val;
    private STATE state;
    private KStack<Point> sol;

    public BreadthFirst(int d) {
        horizontal = new boolean[d + 1][d];
        vertical = new boolean[d][d + 1];
        size = d;

        visited = new int[d][d];
    }

    public void generate() {
        reset();
        path = new KStack<Point>();
        path.push(new Point(0, 0));
        visited[0][0] = 0;
        val = 0;
        state = STATE.FIND;
        sol = new KStack<Point>();

        boolean[][] visited = new boolean[size][size];
        int nVisited = size * size;
        int next;

        Point location = new Point((int) (Math.random() * size),
                (int) (Math.random() * size));

        // location = new Point(size - 1, size - 1);

        ArrayList<Point> path = new ArrayList<Point>();
        ArrayList<Point> empty;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }

        path.add(location);
        nVisited--;
        visited[location.y][location.x] = true;

        while (nVisited > 0) {
            empty = getEmptyNeighbors(location, visited);

            if (empty.size() > 0) {
                next = (int) (Math.random() * empty.size());
                breakWall(location, empty.get(next));

                location = empty.get(next);
                path.add(location);
                nVisited--;
                visited[location.y][location.x] = true;
            } else {
                path.remove(path.size() - 1);
                location = path.get(path.size() - 1);
            }
        }
    }

    private ArrayList<Point> getEmptyNeighbors(Point current,
            boolean[][] visited) {
        ArrayList<Point> choices = new ArrayList<Point>();

        if (current.y != size - 1 && !visited[current.y + 1][current.x])
            choices.add(new Point(current.x, current.y + 1));

        if (current.x != size - 1 && !visited[current.y][current.x + 1])
            choices.add(new Point(current.x + 1, current.y));

        if (current.y != 0 && !visited[current.y - 1][current.x])
            choices.add(new Point(current.x, current.y - 1));

        if (current.x != 0 && !visited[current.y][current.x - 1])
            choices.add(new Point(current.x - 1, current.y));

        return choices;
    }

    private void breakWall(Point current, Point next) {
        int r = Math.max(current.y, next.y);
        int c = Math.max(current.x, next.x);

        if (current.y == next.y) {
            vertical[r][c] = false;
        } else {
            horizontal[r][c] = false;
        }
    }

    private void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                horizontal[j][i] = true;
                vertical[i][j] = true;

                if (j < size) {
                    visited[i][j] = -1;
                }
            }
        }

        done = false;
    }

    @Override
    public void tick() {
        switch (state) {
        case FIND:
            nextIter();
            break;
        case TRACE:
            back();
            break;
        }
    }

    private void back() {
        try {
            Point top = sol.peek();

            if (top.x > 0
                    && visited[top.y][top.x - 1] == visited[top.y][top.x] - 1
                    && !vertical[top.y][top.x]) {
                sol.push(new Point(top.x - 1, top.y));
            } else if (top.y > 0
                    && visited[top.y - 1][top.x] == visited[top.y][top.x] - 1
                    && !horizontal[top.y][top.x]) {
                sol.push(new Point(top.x, top.y - 1));
            } else if (top.y < size - 1
                    && visited[top.y + 1][top.x] == visited[top.y][top.x] - 1
                    && !horizontal[top.y + 1][top.x]) {
                sol.push(new Point(top.x, top.y + 1));
            } else if (top.x < size - 1
                    && visited[top.y][top.x + 1] == visited[top.y][top.x] - 1
                    && !vertical[top.y][top.x + 1]) {
                sol.push(new Point(top.x + 1, top.y));
            }

            if (top.x == 0 && top.y == 0)
                done = true;
        } catch (KInvalidException e) {

        }
    }

    private void nextIter() {
        try {
            KStack<Point> next = new KStack<Point>();
            val++;

            while (path.length() > 0) {
                Point top = path.pop();
                if (top.x > 0 && visited[top.y][top.x - 1] == -1
                        && !vertical[top.y][top.x]) {
                    next.push(new Point(top.x - 1, top.y));
                    visited[top.y][top.x - 1] = val;
                }
                if (top.y > 0 && visited[top.y - 1][top.x] == -1
                        && !horizontal[top.y][top.x]) {
                    next.push(new Point(top.x, top.y - 1));
                    visited[top.y - 1][top.x] = val;
                }
                if (top.y < size - 1 && visited[top.y + 1][top.x] == -1
                        && !horizontal[top.y + 1][top.x]) {
                    next.push(new Point(top.x, top.y + 1));
                    visited[top.y + 1][top.x] = val;
                }
                if (top.x < size - 1 && visited[top.y][top.x + 1] == -1
                        && !vertical[top.y][top.x + 1]) {
                    next.push(new Point(top.x + 1, top.y));
                    visited[top.y][top.x + 1] = val;
                }
            }

            path = next;
            if (visited[size - 1][size - 1] > 0) {
                state = STATE.TRACE;
                sol.push(new Point(size - 1, size - 1));
                val++;
            }
        } catch (KInvalidException e) {

        }
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void display(Graphics g, int width, int height) {
        int vx, vy;
        int hx, hy;

        int boxW = (width - 20) / size;
        int boxH = (height - 20) / size;

        int v = 0xFF;

        for (int i = 0; i < size; i++) {
            hx = 10 + i * boxW;
            vy = 10 + i * boxH;

            for (int j = 0; j < size + 1; j++) {
                g.setColor(Color.black);
                vx = 10 + j * boxW;
                hy = 10 + j * boxH;

                if (horizontal[j][i]) {
                    g.drawLine(hx, hy, hx + boxW, hy);
                }

                if (vertical[i][j]) {
                    g.drawLine(vx, vy, vx, vy + boxH);
                }

                if (j < visited[i].length && visited[i][j] != -1 && val > 0) {
                    int per = v * visited[i][j] / val;
                    if (visited[i][j] == val)
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(new Color(0, per, 0xFF - per));

                    g.fillRect(11 + j * boxW, 11 + i * boxH, boxW - 2, boxH - 2);
                }
            }
        }

        g.setColor(Color.RED);

        Iterator<Point> lst = sol.iterator();
        while (lst.hasNext()) {
            Point p = lst.next();

            g.fillRect(11 + p.x * boxW, 11 + p.y * boxH, boxW - 2, boxH - 2);
        }
    }

    @Override
    public String getName() {
        return "Breadth First Search";
    }
}
