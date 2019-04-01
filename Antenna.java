package sample;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Antenna {
    String MAC;
    double RSSI;
    final double TxPower = -60;
    final double n = 4.2119;
    Point pos;
    AntennaLog log;
    private Date time = null;

    public Antenna(int x, int y, String MAC) {
        this.pos = new Point(x,y);
        this.MAC = MAC;
    }

    public Antenna(int x, int y, String MAC, double RSSI) {
        this.pos = new Point(x,y);
        this.MAC = MAC;
        this.RSSI = RSSI;
    }

    public Antenna update(AntennaLogLine logLine) {
        this.RSSI = logLine.getRSSI();
        this.time = new Date(logLine.timeStamp.getTime());
        return this;
    }

    static Boolean updatable(ArrayList<Antenna> antennas, ArrayList<AntennaLogLine> logLines) {
        int n=0;

        for (AntennaLogLine logLine:logLines) {
            for (Antenna antenna:antennas) {
                if (logLine.getMAC().equals(antenna.MAC)) {
                    if (antenna.time==null || !antenna.time.equals(logLine.timeStamp)) n++;
                }
            }
        }
        if (n==antennas.size()) return true;
        else return false;
    }

    public double singalToDistance() {
        return Math.pow(10,((this.TxPower-this.RSSI)/(this.n*10)));
    }

    public double signalToDistaince(double signal) {
        return Math.pow(10,((this.TxPower-signal)/(this.n*10)));
    }

    public double distanceToSignal(double distance) {
        return -n*10*Math.log10(distance)+TxPower;
    }

    public void drawAntennaRange(Graphics g) {
       final int n = 100;
       final int radius =  2;

       double d = n*singalToDistance();

       g.setColor(Color.BLACK);
       g.drawOval(pos.getX()-(int)d,pos.getY()-(int)d,2*(int)d,2*(int)d);

       g.setColor(Color.BLACK);
       g.fillOval(pos.getX()-radius,pos.getY()-radius,radius*2,radius*2);
    }

    public  AntennaLog importLog(String directory) throws IOException {
        this.log = new AntennaLog();
        this.log.addLog(directory);
        return this.log;
    }

    public AntennaLog printLog() {
        log.printLog();
        return this.log;
    }

    public ArrayList<String> getLog() {
        ArrayList<String> logList = new ArrayList<>();
        for (AntennaLogLine antennaLogLine : this.log.getLog()) {
            logList.add(antennaLogLine.getLogLine());
        }
        return logList;
    }

    public String getMAC() {
        return this.MAC;
    }

    public double getRSSI() {
        return this.RSSI;
    }

    static ArrayList<Circle> getStrongest(ArrayList<Circle> circles, int numberOfSignals) {
        ArrayList<Circle> strongest = new ArrayList<>();
        ArrayList<Circle> circleList = new ArrayList<>(circles);
        int strongestID;

        for (int i=0; i<numberOfSignals; i++) {
            strongestID=0;
            int k=0;
            for (Circle circle : circleList) {
                if (circle.getRadius() < circleList.get(strongestID).getRadius()) {
                    strongestID = k;
                }
                k++;
            }
            strongest.add(circleList.get(strongestID));
            circleList.remove(strongestID);
        }
        return strongest;
    }

    static Point findSignal(ArrayList<Antenna> antennas)  throws Exception {
        ArrayList<Circle> circles = new ArrayList<>();
        for (Antenna antenna : antennas) {
            circles.add(new Circle(antenna.singalToDistance(), new Point(antenna.pos.getX(), antenna.pos.getY())));
        }
        if (antennas.size()==3) return findSignalOfThree(circles);
        if (antennas.size()==4) return findSignalOfFour(circles);
        if (antennas.size()>4) return findSignalOfFour(getStrongest(circles, 4));
        else throw new Exception("At least 3 antennas expected");
    }

    private static Point findSignalOfFour(ArrayList<Circle> circles) throws Exception{
        if (circles.size()!=4) throw new Exception("Passed argument must be four-element list");

        ArrayList<Point> points = Circle.getPoints(circles);
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
                    if (circle.getPosition().isEqual(p[i])) c[i] = new Circle(circle);
                }
            }

            double proportionFactor1 = d1/(c[0].getRadius()+c[1].getRadius());
            double proportionFactor2 = d2/(c[2].getRadius()+c[3].getRadius());

            Circle tmp1 = new Circle(c[0]);
            Circle tmp2 = new Circle(c[1]);

            Circle tmp3 = new Circle(c[2]);
            Circle tmp4 = new Circle(c[3]);

            tmp1.setRadius(tmp1.getRadius()*proportionFactor2);
            tmp2.setRadius(tmp2.getRadius()*proportionFactor2);

            tmp3.setRadius(tmp3.getRadius()*proportionFactor1);
            tmp4.setRadius(tmp4.getRadius()*proportionFactor1);

            if (tmp1.doIntersect(tmp2)) {
                for (Circle circle : c) {
                    circle.setRadius(circle.getRadius() * proportionFactor2);
                }
            }
            else if (tmp3.doIntersect(tmp4)) {
                for (Circle circle : c) {
                    circle.setRadius(circle.getRadius() * proportionFactor1);
                }
            }
            else {
                throw new Exception("Unable to find signal");
            }

            ArrayList<Circle> circleList = new ArrayList<>();
            circleList.add(c[0]);
            circleList.add(c[1]);
            circleList.add(c[2]);
            circleList.add(c[3]);

            ArrayList<Point> approximationPoints = new ArrayList<>();
            approximationPoints.add(Circle.findMatchingPoint(circleList,Circle.findCircleIntersectionPoints(c[0],c[1])));
            approximationPoints.add(Circle.findMatchingPoint(circleList,Circle.findCircleIntersectionPoints(c[2],c[3])));
            approximationPoints.add(Circle.findMatchingPoint(circleList,Circle.findCircleIntersectionPoints(c[0],c[2])));
            approximationPoints.add(Circle.findMatchingPoint(circleList,Circle.findCircleIntersectionPoints(c[1],c[3])));

            return Point.getMiddlePoint(approximationPoints);
        }
        else throw new Exception("Unable to find signal");
    }

    private static Point findSignalOfThree(ArrayList<Circle> circles) throws Exception {
        if (circles.size()!=3) throw new Exception("Passed argument must be three-element list");

        ArrayList<Point> approximationPoints = new ArrayList<>();

        for (int i=0; i<3; i++) {
            double d = Point.distanceBetween(circles.get(i).getPosition(), circles.get((i+1)%3).getPosition());
            double proportionFactor = d/(circles.get(i).getRadius()+circles.get((i+1)%3).getRadius());

            ArrayList<Circle> tmp = new ArrayList<>();
            for (int j=0; j<3; j++) {
                tmp.add(circles.get(j));
                tmp.get(j).setRadius(tmp.get(j).getRadius() * proportionFactor);
            }

            ArrayList<Point> iterationApproximation = new ArrayList<>();
            for (int j=0; j<3; j++) {
                iterationApproximation.add(Circle.findMatchingPoint(tmp,Circle.findCircleIntersectionPoints(tmp.get(j),tmp.get((j+1)%3))));
            }
            approximationPoints.add(Point.getMiddlePoint(iterationApproximation));
        }

        return Point.getMiddlePoint(approximationPoints);
    }

    public static Map<Date,Point> getAllApproximationsWithTime(AntennaLog log, ArrayList<Antenna> antennaList) {
        Map<Date,Point> foundPoints = new HashMap<>();
        ArrayList<Antenna> antennas = duplicateAntennaList(antennaList);

        ArrayList<Date> dates = log.getAllDates();
        for (Date date:dates) {
            ArrayList<AntennaLogLine> logLines = log.getAll(date);

            if (Antenna.updatable(antennas,logLines)) {
                for (Antenna antenna : antennas) {
                    for (AntennaLogLine logLine : logLines) {
                        if (logLine.MAC.equals(antenna.MAC)) {
                            antenna.update(logLine);
                        }
                    }
                }
                try {
                    foundPoints.put(date, findSignal(antennas));
                } catch (Exception ignored){}
            }
        }

        return foundPoints;
    }

    public static ArrayList<Point> getAllApproximations(AntennaLog log, ArrayList<Antenna> antennaList) {
        ArrayList<Point> foundPoints = new ArrayList<>();
        ArrayList<Antenna> antennas = duplicateAntennaList(antennaList);

        ArrayList<Date> dates = log.getAllDates();

        for (Date date:dates) {
            ArrayList<AntennaLogLine> logLines = log.getAll(date);

            if (Antenna.updatable(antennas,logLines)) {
                for (Antenna antenna : antennas) {
                    for (AntennaLogLine logLine : logLines) {
                        if (logLine.MAC.equals(antenna.MAC)) {
                            antenna.update(logLine);
                        }
                    }
                }
                try {
                    foundPoints.add(findSignal(antennas));
                } catch (Exception ignored){}
            }
        }

        return foundPoints;
    }

    public static ArrayList<Antenna> duplicateAntennaList(ArrayList<Antenna> antennas) {
        ArrayList<Antenna> duplicate =  new ArrayList<>();
        for (Antenna antenna:antennas) {
            duplicate.add(new Antenna(antenna.pos.getX(), antenna.pos.getY(), antenna.getMAC(), antenna.getRSSI()));
        }

        return duplicate;
    }
}
