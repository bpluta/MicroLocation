package sample;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Circle {
    private double radius;
    private Point position;

    Circle(double r, Point p) {
        this.radius = r;
        position = new Point(p);
    }

    Circle(Circle a) {
        this.radius = a.radius;
        position = new Point(a.position);
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public double getRadius() {
        return this.radius;
    }

    public Point getPosition() {
        return this.position;
    }

    public Point setPosition(Point point) {
        this.position = new Point(point);
        return this.position;
    }

    public double setRadius(double radius) {
        this.radius = radius;
        return this.radius;
    }

    public static void drawCircles(Graphics g, ArrayList<Circle> circles) {
        for (Circle object: circles){
            object.position.drawPoint(g);
            object.drawCircle(g);
        }
    }

    public void drawCircle(Graphics g) {
        g.drawOval((int)(this.position.x-this.radius),(int)(this.position.y-this.radius),(int)this.radius*2,(int)this.radius*2);
    }

    public static double distanceBetween(Circle a, Circle b) {
        return Point.distanceBetween(a.position,b.position);
    }

    public static double distanceBetween(Circle a, Point b) {
        return Math.abs(Point.distanceBetween(a.position,b)-a.radius);
    }

    public static Boolean doIntersect(Circle a, Circle b) {
        double d = Point.distanceBetween(a.position, b.position);
        if (d == (a.radius + b.radius)) return true;
        if (Math.abs(a.radius - b.radius) < d && d < (a.radius + b.radius)) return true;
        else if (Math.abs(a.radius - b.radius) == d) return true;
        return false;
    }

    public Boolean doIntersect(Circle a) {
        double d = Point.distanceBetween(a.position, this.position);
        if (d == (a.radius + this.radius)) return true;
        if (Math.abs(a.radius - this.radius) < d && d < (a.radius + this.radius)) return true;
        else if (Math.abs(a.radius - this.radius) == d) return true;
        return false;
    }

    public static ArrayList<Point> getPoints(ArrayList<Circle> circles) {
        ArrayList<Point> points = new ArrayList<>();
        for (Circle object: circles) {
            points.add(object.position);
        }
        return points;
    }

    public static ArrayList<Point> getCircleIntersection(Circle c1, Circle c2) {

        double d = distanceBetween(c1,c2);
        ArrayList<Point> intersectionPoints = new ArrayList<>();

        if (d == (c1.radius + c2.radius)) {
            Point point = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                    c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))));

            intersectionPoints.add(point);
            return intersectionPoints;
        }

        if (d > (c2.radius + c2.radius)) {
            Point p1, p2;

            p1 = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                    c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX()- c1.getX()), (c2.getY() - c1.getY()))));

            p2 = new Point(c2.getX() + c2.radius * ((c1.getX() - c2.getX()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))),
                    c2.getY() + c2.radius * ((c1.getY() - c2.getY()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))));

            intersectionPoints.add(p1);
            intersectionPoints.add(p2);
            return intersectionPoints;
        }

        if ((int)d == (int)(c1.radius + c2.radius) || (int)d == (int)Math.abs(c1.radius - c2.radius)) {
            int x = (int)(c1.position.x + c1.radius * ((c2.position.x - c1.position.x) / Math.hypot((c2.position.x - c1.position.x), (c2.position.y - c1.position.y))));
            int y = (int)(c1.position.y + c1.radius * ((c2.position.y - c1.position.y) / Math.hypot((c2.position.x - c1.position.x), (c2.position.y - c1.position.y))));

            intersectionPoints.add(new Point(x,y));
            return intersectionPoints;
        }

        if (d>Math.abs(c1.radius-c2.radius) && d<Math.abs(c1.radius+c2.radius)) {
            Point p1, p2;

            if (c1.radius < c2.radius) {
                p1 = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                        c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))));

                p2 = new Point(c2.getX() + c2.radius * ((c1.getX() - c2.getX()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))),
                        c2.getY() + c2.radius * ((c1.getY() - c2.getY()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))));

                intersectionPoints.add(p1);
                intersectionPoints.add(p2);
                return intersectionPoints;

            } else {
                p1 = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                        c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))));

                p2 = new Point(c2.getX() + c2.radius * ((c1.getX() - c2.getX()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))),
                        c2.getY() + c2.radius * ((c1.getY() - c2.getY()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))));

                intersectionPoints.add(p1);
                intersectionPoints.add(p2);
                return intersectionPoints;
            }
        }

        return intersectionPoints;
    }

    public static ArrayList<Point> findCircleIntersectionPoints(Circle c1, Circle c2) {
        double d = distanceBetween(c1,c2);
        ArrayList<Point> intersectionPoints = new ArrayList<>();

        if (d == (c1.radius + c2.radius)) {
            Point point = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                    c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))));

            intersectionPoints.add(point);
            return intersectionPoints;
        }

        if (d > (c1.radius + c2.radius)) {
            Point p1, p2;

            p1 = new Point(c1.getX() + c1.radius * ((c2.getX() - c1.getX()) / Math.hypot((c2.getX() - c1.getX()), (c2.getY() - c1.getY()))),
                    c1.getY() + c1.radius * ((c2.getY() - c1.getY()) / Math.hypot((c2.getX()- c1.getX()), (c2.getY() - c1.getY()))));

            p2 = new Point(c2.getX() + c2.radius * ((c1.getX() - c2.getX()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))),
                    c2.getY() + c2.radius * ((c1.getY() - c2.getY()) / Math.hypot((c1.getX() - c2.getX()), (c1.getY() - c2.getY()))));

            intersectionPoints.add(p1);
            intersectionPoints.add(p2);

            return intersectionPoints;
        }

        if (d>Math.abs(c1.radius-c2.radius) && d<Math.abs(c1.radius+c2.radius)) {
            double d1 = (Math.pow(c1.radius, 2) - Math.pow(c2.radius, 2) + Math.pow(d, 2)) / (2 * d);
            double h = Math.sqrt(Math.pow(c1.radius, 2) - Math.pow(d1, 2));
            double x = c1.getX() + (d1 * (c2.getX() - c1.getX())) / d;
            double y = c1.getY() + (d1 * (c2.getY() - c1.getY())) / d;

            Point point1 = new Point(x + (h * (c2.getY() - c1.getY())) / d, y - (h * (c2.getX() - c1.getX())) / d);
            Point point2 = new Point(x - (h * (c2.getY() - c1.getY())) / d, y + (h * (c2.getX() - c1.getX())) / d);

            intersectionPoints.add(point1);
            intersectionPoints.add(point2);
            return (intersectionPoints);
        }
        return intersectionPoints;
    }

    public static Point findMatchingPoint(ArrayList<Circle> circles, ArrayList<Point> points) {

        if (points.size()==1) {
            return new Point(points.get(0));
        }
        else if (points.size()>1) {
            double[] averageDistances = new double[points.size()];
            int n=0;

            for (Point point:points) {
                averageDistances[n] = 0;
                for (Circle circle : circles) {
                    averageDistances[n] += Circle.distanceBetween(circle, point);
                }
                averageDistances[n]/=points.size();
                n++;
            }
            int shortestIndex = 0;
            double shortestDistance = averageDistances[shortestIndex];
            for (int i=0; i<averageDistances.length; i++) {
                if (averageDistances[i]<shortestDistance) {
                    shortestDistance = averageDistances[i];
                    shortestIndex = i;
                }
            }
            return new Point(points.get(shortestIndex));
        }
        return null;
    }

    public static Point getSignalPosition(ArrayList<Circle> circles, Graphics g) throws Exception{
        ArrayList<Point> points = Circle.getPoints(circles);

        if (points.size()==4) {
            ArrayList<LineSegment> segments = LineSegment.findIntersection(points);
            if (segments.size()==2) {
                double d1 = segments.get(0).length();
                double d2 = segments.get(1).length();
                Point[] p = new Point[4];
                Circle[] c = new Circle[4];

                p[0] = segments.get(0).a;
                p[1] = segments.get(0).b;
                p[2] = segments.get(1).a;
                p[3] = segments.get(1).b;

                for (int i=0; i<c.length; i++) {
                    for (Circle circle:circles) {
                        if (circle.position.isEqual(p[i])) c[i] = new Circle(circle);
                    }
                }

                double proportionFactor1 = d1/(c[0].radius+c[1].radius);
                double proportionFactor2 = d2/(c[2].radius+c[3].radius);

                Circle tmp1 = new Circle(c[0]);
                Circle tmp2 = new Circle(c[1]);

                Circle tmp3 = new Circle(c[2]);
                Circle tmp4 = new Circle(c[3]);

                tmp1.setRadius(tmp1.radius*proportionFactor2);
                tmp2.setRadius(tmp2.radius*proportionFactor2);

                tmp3.setRadius(tmp3.radius*proportionFactor1);
                tmp4.setRadius(tmp4.radius*proportionFactor1);

                if (tmp1.doIntersect(tmp2)) {
                    for (Circle circle : c) {
                        circle.setRadius(circle.radius * proportionFactor2);
                    }
                }
                else if (tmp3.doIntersect(tmp4)) {
                    for (Circle circle : c) {
                        circle.setRadius(circle.radius * proportionFactor1);
                    }
                }
                else {
                    throw new Exception("No intersection found");
                }

                ArrayList<Circle> circleList = new ArrayList<>();
                circleList.add(c[0]);
                circleList.add(c[1]);
                circleList.add(c[2]);
                circleList.add(c[3]);

                ArrayList<Point> approximationPoints = new ArrayList<>();
                approximationPoints.add(findMatchingPoint(circleList,findCircleIntersectionPoints(c[0],c[1])));
                approximationPoints.add(findMatchingPoint(circleList,findCircleIntersectionPoints(c[2],c[3])));
                approximationPoints.add(findMatchingPoint(circleList,findCircleIntersectionPoints(c[0],c[2])));
                approximationPoints.add(findMatchingPoint(circleList,findCircleIntersectionPoints(c[1],c[3])));

                Point finalPoint = Point.getMiddlePoint(approximationPoints);

                for (int i=0;i< c.length; i++) {
                    if (i==2) g.setColor(Color.BLUE);
                    c[i].drawCircle(g);
                }
                g.setColor(Color.BLACK);

                g.setColor(Color.GRAY);
                for (Point approximationPoint : approximationPoints) {
                    approximationPoint.drawPoint(g);
                }
                g.setColor(Color.BLACK);

                g.setColor(Color.RED);
                finalPoint.drawPoint(g);
                g.setColor(Color.BLACK);
                return finalPoint;
            }
            else throw new Exception("Error");
        }
        else throw new Exception("Error");

    }
}
