package sample;

import java.awt.*;
import java.util.ArrayList;

public class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y) {
        this.x = (int)x;
        this.y = (int)y;
    }

    Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void drawPoint(Graphics g) {
        int pointRadius = 5;
        g.fillOval(this.x-pointRadius,this.y-pointRadius,pointRadius*2,pointRadius*2);
    }

    public static void drawPoints(Graphics g, ArrayList<Point> points) {
        int pointRadius = 5;
        for (Point point:points) {
            g.fillOval(point.x-pointRadius,point.y-pointRadius,pointRadius*2,pointRadius*2);
        }
    }

    public static double distanceBetween(Point a, Point b) {
        return Math.hypot(a.x-b.x, a.y-b.y);
    }

    public double distanceBetween(Point a) {
        return Math.hypot(a.x-this.x, a.y-this.y);
    }

    public static Point getMiddlePoint(ArrayList<Point> points) {
        if (points.size()!=0) {
            int n = 0, x = 0, y = 0;
            for (Point point:points) {
                x += point.x;
                y += point.y;
                n++;
            }
            return new Point(x / n, y / n);
        }
        else return new Point(0,0);
    }

    public Boolean isEqual(Point a) {
        return this.x == a.x && this.y == a.y;
    }
}
