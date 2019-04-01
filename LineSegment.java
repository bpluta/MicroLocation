package sample;

import java.awt.*;
import java.util.ArrayList;

public class LineSegment {
    public Point a;
    public Point b;

    LineSegment(int a[], int b[]) {
        this.a = new Point(a[0],a[1]);
        this.b = new Point(b[0],b[1]);
    }

    LineSegment(Point a, Point b) {
        this.a = new Point(a);
        this.b = new Point(b);
    }

    public double length() {
        return this.a.distanceBetween(this.b);
    }

    public void drawLineSegment(Graphics g) {
        g.drawLine(a.x, a.y, b.x, b.y);
        a.drawPoint(g);
        b.drawPoint(g);
    }

    public static void drawLineSegment(Graphics g, LineSegment segment) {
        g.drawLine(segment.a.x,segment.a.y,segment.b.x,segment.b.y);
        segment.a.drawPoint(g);
        segment.b.drawPoint(g);
    }

    public static void drawLineSegments(Graphics g, ArrayList<LineSegment> segments) {
        LineSegment object;
        for (LineSegment segment : segments) {
            object = segment;
            g.drawLine(object.a.x, object.a.y, object.b.x, object.b.y);
            object.a.drawPoint(g);
            object.b.drawPoint(g);
        }
    }

    private static int orientation(Point point1, Point point2, Point point3) {
        int val = (point2.y - point1.y)*(point3.x - point2.x) - (point2.x - point1.x)*(point3.y - point2.y);
        if (val == 0) return 0;
        if (val>0) return 1;
        else return 2;
    }

    private static Boolean onSegment(Point point1, Point point2, Point point3) {
        if (point2.x <= Math.max(point1.x, point3.x) && point2.x >= Math.min(point1.x, point3.x) &&
                point2.y <= Math.max(point1.y, point3.y) && point2.y >= Math.min(point1.y, point3.y))
            return true;

        return false;
    }

    public static Boolean doIntersect(LineSegment s1, LineSegment s2) {
        int o1 = orientation(s1.a, s1.b, s2.a);
        int o2 = orientation(s1.a, s1.b, s2.b);
        int o3 = orientation(s2.a, s2.b, s1.a);
        int o4 = orientation(s2.a, s2.b, s1.b);

        if (o1!=o2 && o3!=o4) return true;

        if (o1 == 0 && onSegment(s1.a, s2.a, s1.b)) return true;
        if (o2 == 0 && onSegment(s1.a, s2.b, s1.b)) return true;
        if (o3 == 0 && onSegment(s2.a, s1.a, s2.b)) return true;
        if (o4 == 0 && onSegment(s2.a, s1.b, s2.b)) return true;

        return false;
    }

    public static ArrayList<LineSegment> findIntersection(ArrayList<Point> points) {
        ArrayList<LineSegment> list = new ArrayList<>();
        LineSegment s1, s2;

        s1 = new LineSegment(points.get(0), points.get(1));
        s2 = new LineSegment(points.get(2), points.get(3));
        if (doIntersect(s1, s2)) {
            list.add(s1);
            list.add(s2);
            return list;
        }

        s1 = new LineSegment(points.get(0), points.get(2));
        s2 = new LineSegment(points.get(1), points.get(3));
        if (doIntersect(s1, s2)) {
            list.add(s1);
            list.add(s2);
            return list;
        }

        s1 = new LineSegment(points.get(0), points.get(3));
        s2 = new LineSegment(points.get(1), points.get(2));
        if (doIntersect(s1, s2)) {
            list.add(s1);
            list.add(s2);
            return list;
        }
        return list;
    }
}
