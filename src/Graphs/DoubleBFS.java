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

public class DoubleBFS extends Algorithm {
    public static void main(String[] args) throws Exception {
        DoubleBFS df = new DoubleBFS(100);
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

    private class Square {
        int val = 0;
        boolean visited = false;
    }

    private boolean[][] horizontal;
    private boolean[][] vertical;
    private int size;

    private boolean done;
    private Square[][] visited;
    private KStack<Point> path1, path2;
    private int val;
    private STATE state;
    private KStack<Point> solPos, solNeg;

    public DoubleBFS(int d) {
        horizontal = new boolean[d + 1][d];
        vertical = new boolean[d][d + 1];
        size = d;

        visited = new Square[d][d];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = new Square();
            }
        }
    }

    public void generate() {
        reset();

        path1 = new KStack<Point>();
        path1.push(new Point(0, 0));
        path2 = new KStack<Point>();
        path2.push(new Point(size - 1, size - 1));

        visited[0][0].val = 0;
        visited[0][0].visited = true;
        visited[size - 1][size - 1].val = 0;
        visited[size - 1][size - 1].visited = true;

        val = 0;
        state = STATE.FIND;
        solNeg = new KStack<Point>();
        solPos = new KStack<Point>();

        boolean[][] visited = new boolean[size][size];
        int nVisited = size * size;
        int next;

        Point location = new Point((int) (Math.random() * size),
                (int) (Math.random() * size));

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
                    visited[i][j].val = -1;
                    visited[i][j].visited = false;
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
            Point top = solNeg.peek();

            if (top.x > 0
                    && visited[top.y][top.x - 1].val == visited[top.y][top.x].val + 1
                    && !vertical[top.y][top.x]) {
                solNeg.push(new Point(top.x - 1, top.y));
            } else if (top.y > 0
                    && visited[top.y - 1][top.x].val == visited[top.y][top.x].val + 1
                    && !horizontal[top.y][top.x]) {
                solNeg.push(new Point(top.x, top.y - 1));
            } else if (top.y < size - 1
                    && visited[top.y + 1][top.x].val == visited[top.y][top.x].val + 1
                    && !horizontal[top.y + 1][top.x]) {
                solNeg.push(new Point(top.x, top.y + 1));
            } else if (top.x < size - 1
                    && visited[top.y][top.x + 1].val == visited[top.y][top.x].val + 1
                    && !vertical[top.y][top.x + 1]) {
                solNeg.push(new Point(top.x + 1, top.y));
            }

            top = solPos.peek();

            if (top.x > 0
                    && visited[top.y][top.x - 1].val == visited[top.y][top.x].val - 1
                    && !vertical[top.y][top.x]) {
                solPos.push(new Point(top.x - 1, top.y));
            } else if (top.y > 0
                    && visited[top.y - 1][top.x].val == visited[top.y][top.x].val - 1
                    && !horizontal[top.y][top.x]) {
                solPos.push(new Point(top.x, top.y - 1));
            } else if (top.y < size - 1
                    && visited[top.y + 1][top.x].val == visited[top.y][top.x].val - 1
                    && !horizontal[top.y + 1][top.x]) {
                solPos.push(new Point(top.x, top.y + 1));
            } else if (top.x < size - 1
                    && visited[top.y][top.x + 1].val == visited[top.y][top.x].val - 1
                    && !vertical[top.y][top.x + 1]) {
                solPos.push(new Point(top.x + 1, top.y));
            }

            if (top.x == 0 && top.y == 0)
                done = true;
        } catch (KInvalidException e) {

        }
    }

    private void nextIter() {
        try {
            Point end1 = null;
            Point end2 = null;

            KStack<Point> next1 = new KStack<Point>();
            val++;

            while (path1.length() > 0) {
                Point top = path1.pop();
                if (top.x > 0 && !vertical[top.y][top.x]) {
                    if (!visited[top.y][top.x - 1].visited) {
                        next1.push(new Point(top.x - 1, top.y));
                        visited[top.y][top.x - 1].val = val;
                        visited[top.y][top.x - 1].visited = true;
                    } else if (visited[top.y][top.x - 1].val < 0) {
                        end1 = new Point(top.x, top.y);
                        end2 = new Point(top.x - 1, top.y);
                    }
                }
                if (top.y > 0 && !horizontal[top.y][top.x]) {
                    if (!visited[top.y - 1][top.x].visited) {
                        next1.push(new Point(top.x, top.y - 1));
                        visited[top.y - 1][top.x].val = val;
                        visited[top.y - 1][top.x].visited = true;
                    } else if (visited[top.y - 1][top.x].val < 0) {
                        end1 = new Point(top.x, top.y);
                        end2 = new Point(top.x, top.y - 1);
                    }
                }
                if (top.y < size - 1 && !horizontal[top.y + 1][top.x]) {
                    if (!visited[top.y + 1][top.x].visited) {
                        next1.push(new Point(top.x, top.y + 1));
                        visited[top.y + 1][top.x].val = val;
                        visited[top.y + 1][top.x].visited = true;
                    } else if (visited[top.y + 1][top.x].val < 0) {
                        end1 = new Point(top.x, top.y);
                        end2 = new Point(top.x, top.y + 1);
                    }
                }
                if (top.x < size - 1 && !vertical[top.y][top.x + 1]) {
                    if (!visited[top.y][top.x + 1].visited) {
                        next1.push(new Point(top.x + 1, top.y));
                        visited[top.y][top.x + 1].val = val;
                        visited[top.y][top.x + 1].visited = true;
                    } else if (visited[top.y][top.x + 1].val < 0) {
                        end1 = new Point(top.x, top.y);
                        end2 = new Point(top.x + 1, top.y);
                    }
                }
            }

            path1 = next1;

            KStack<Point> next2 = new KStack<Point>();

            while (path2.length() > 0) {
                Point top = path2.pop();
                if (top.x > 0 && !vertical[top.y][top.x]) {
                    if (!visited[top.y][top.x - 1].visited) {
                        next2.push(new Point(top.x - 1, top.y));
                        visited[top.y][top.x - 1].val = -val;
                        visited[top.y][top.x - 1].visited = true;
                    } else if (visited[top.y][top.x - 1].val > 0) {
                        end2 = new Point(top.x, top.y);
                        end1 = new Point(top.x - 1, top.y);
                    }
                }
                if (top.y > 0 && !horizontal[top.y][top.x]) {
                    if (!visited[top.y - 1][top.x].visited) {
                        next2.push(new Point(top.x, top.y - 1));
                        visited[top.y - 1][top.x].val = -val;
                        visited[top.y - 1][top.x].visited = true;
                    } else if (visited[top.y - 1][top.x].val > 0) {
                        end2 = new Point(top.x, top.y);
                        end1 = new Point(top.x, top.y - 1);
                    }
                }
                if (top.y < size - 1 && !horizontal[top.y + 1][top.x]) {
                    if (!visited[top.y + 1][top.x].visited) {
                        next2.push(new Point(top.x, top.y + 1));
                        visited[top.y + 1][top.x].val = -val;
                        visited[top.y + 1][top.x].visited = true;
                    } else if (visited[top.y + 1][top.x].val > 0) {
                        end2 = new Point(top.x, top.y);
                        end1 = new Point(top.x, top.y + 1);
                    }
                }
                if (top.x < size - 1 && !vertical[top.y][top.x + 1]) {
                    if (!visited[top.y][top.x + 1].visited) {
                        next2.push(new Point(top.x + 1, top.y));
                        visited[top.y][top.x + 1].val = -val;
                        visited[top.y][top.x + 1].visited = true;
                    } else if (visited[top.y][top.x + 1].val > 0) {
                        end2 = new Point(top.x, top.y);
                        end1 = new Point(top.x + 1, top.y);
                    }
                }
            }

            path2 = next2;

            if (end2 != null && end1 != null) {
                solPos.push(end1);
                solNeg.push(end2);

                state = STATE.TRACE;
            }
        } catch (KInvalidException e) {

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

                if (j < visited[i].length && visited[i][j].visited && val > 0) {
                    int per = v * Math.abs(visited[i][j].val) / val;
                    if (visited[i][j].val < 0)
                        g.setColor(new Color(0, per, 0));
                    else if (visited[i][j].val > 0)
                        g.setColor(new Color(0, 0, per));
                    else if (visited[i][j].val == val)
                        g.setColor(Color.BLUE);
                    else if (visited[i][j].val == -val)
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(Color.BLACK);

                    g.fillRect(11 + j * boxW, 11 + i * boxH, boxW - 2, boxH - 2);
                }
            }
        }

        g.setColor(Color.RED);

        Iterator<Point> lst = solNeg.iterator();
        while (lst.hasNext()) {
            Point p = lst.next();

            g.fillRect(11 + p.x * boxW, 11 + p.y * boxH, boxW - 2, boxH - 2);
        }
        lst = solPos.iterator();
        while (lst.hasNext()) {
            Point p = lst.next();

            g.fillRect(11 + p.x * boxW, 11 + p.y * boxH, boxW - 2, boxH - 2);
        }
    }

    @Override
    public String getName() {
        return "Double Breadth First Search";
    }
}
