package sample;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import javax.swing.JOptionPane;

public class Main {
    ArrayList<Antenna> antennas = new ArrayList<>();
    AntennaLog log = new AntennaLog();
    ArrayList<Point> pointList;

    Main() {
        Frame f = new Frame("MicroLocation");
        f.add(new MyCanvas());
        f.setLayout(null);
        f.setSize(500, 500);
        f.setVisible(true);

        antennas.add(new Antenna(450,450, "AA:AA:AA:AA:AA:AA"));
        antennas.add(new Antenna(450,50, "BB:BB:BB:BB:BB:BB"));
        antennas.add(new Antenna(50,450, "CC:CC:CC:CC:CC:CC"));
        antennas.add(new Antenna(50,50, "DD:DD:DD:DD:DD:DD"));

        try {
            log.addLog("./log.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error occoured", JOptionPane.ERROR_MESSAGE);
        }

        pointList = Antenna.getAllApproximations(log, antennas);
        Iterator<Point> it = pointList.iterator();

        while (it.hasNext()) {
            Point p = it.next();
            System.out.println(p.getX() + " " + p.getY());
        }
    }

    public static void main(String args[]) {
        Main window = new Main();
    }

    class MyCanvas extends Canvas {

        public MyCanvas() {
            setBackground(Color.WHITE);
            setSize(500, 500);
        }

        public void paint(Graphics g) {
            //Drawing antennas location
            g.setColor(Color.BLACK);
            for (int i=0; i<antennas.size();i++) {
                antennas.get(i).pos.drawPoint(g);
            }

            //Drawing signal locations
            g.setColor(Color.RED);
            try {
                Thread.sleep(100);
                for (int i=0; i<pointList.size(); i++) {
                    pointList.get(i).drawPoint(g);
                    Thread.sleep(100);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
