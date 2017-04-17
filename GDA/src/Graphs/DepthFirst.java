package Graphs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import Core.Algorithm;
import Core.GDAForm;
import LinkedLibraries.Lists.KInvalidException;
import LinkedLibraries.Lists.KStack;

public class DepthFirst implements Algorithm {
    public static void main(String[] args) throws Exception {
        DepthFirst df = new DepthFirst(100);
        GDAForm form = new GDAForm(df, 1000, 1000, 10);

        while (true) {
            df.generate();
            form.run();

            Thread.sleep(1000);
        }
    }

    private boolean[][] horizontal;
    private boolean[][] vertical;
    private int size;

    private boolean done;
    private boolean[][] visited;
    private boolean[][] inStack;
    private KStack<Point> path;

    public DepthFirst(int d) {
        horizontal = new boolean[d + 1][d];
        vertical = new boolean[d][d + 1];
        size = d;

        visited = new boolean[d][d];
        inStack = new boolean[d][d];
    }

    public void generate() {
        reset();
        path = new KStack<Point>();
        path.push(new Point(0, 0));
        visited[0][0] = true;
        inStack[0][0] = true;

        boolean[][] visited = new boolean[size][size];
        int nVisited = size * size;
        int next;

        Point location = new Point((int) (Math.random() * size),
                (int) (Math.random() * size));

        //location = new Point(size - 1, size - 1);

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
                    visited[i][j] = false;
                    inStack[i][j] = false;
                }
            }
        }

        done = false;
    }

    @Override
    public void tick() {
        try {
            Point top = path.peek();

            ArrayList<Point> next = new ArrayList<Point>(4);

            if (top.x > 0 && !visited[top.y][top.x - 1]
                    && !vertical[top.y][top.x]) {
                next.add(new Point(top.x - 1, top.y));
            }
            if (top.y > 0 && !visited[top.y - 1][top.x]
                    && !horizontal[top.y][top.x]) {
                next.add(new Point(top.x, top.y - 1));
            }
            if (top.y < size - 1 && !visited[top.y + 1][top.x]
                    && !horizontal[top.y + 1][top.x]) {
                next.add(new Point(top.x, top.y + 1));
            }
            if (top.x < size - 1 && !visited[top.y][top.x + 1]
                    && !vertical[top.y][top.x + 1]) {
                next.add(new Point(top.x + 1, top.y));
            }

            if (next.size() == 0) {
                path.pop();
                inStack[top.y][top.x] = false;
            } else {
                Point n = next.get((int) (Math.random() * next.size()));
                visited[n.y][n.x] = true;
                inStack[n.y][n.x] = true;
                path.push(n);

                if (n.x == size - 1 && n.y == size - 1)
                    done = true;
            }
        } catch (KInvalidException e) {
            e.printStackTrace();
            System.exit(0);
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

                if (j < visited[i].length && visited[i][j]) {
                    if (inStack[i][j])
                        g.setColor(Color.GREEN);
                    else if (visited[i][j])
                        g.setColor(Color.RED);

                    g.fillRect(11 + j * boxW, 11 + i * boxH, boxW - 2, boxH - 2);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Depth First Search";
    }

}
