import java.awt.*;
import java.util.*;
import java.util.List;

public class TelNet {
    private final int lbg;
    private final Map<TelKnoten, Integer> knoten;
    private final List<TelVerbindung> optimalesNetz;

    public TelNet(int lbg) {
        this.lbg = lbg;
        knoten = new HashMap<>();
        optimalesNetz = new LinkedList<>();
    }

    public boolean addTelKnoten(int x, int y) {
        TelKnoten telKnoten = new TelKnoten(x, y);
        if (!knoten.containsKey(telKnoten)) {
            this.knoten.put(new TelKnoten(x, y), knoten.size());
            return true;
        } else return false;
    }

    public boolean computeOptTelNet() {
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>();
        UnionFind union = new UnionFind(knoten.size());

        knoten.keySet().forEach(from -> knoten.keySet().forEach(to -> {
            if (from != to) {
                TelVerbindung t = new TelVerbindung(from, to);

                if (t.dist <= lbg)
                    edges.add(t);
            }
        }));

        while (union.size() > 1 && !edges.isEmpty()) {
            TelVerbindung verbindung = edges.poll();

            int t1 = union.find(knoten.get(verbindung.from));
            int t2 = union.find(knoten.get(verbindung.to));

            if (t1 != t2) {
                union.union(t1, t2);
                optimalesNetz.add(verbindung);
            }
        }


        if (edges.isEmpty() && union.size() != 1) {
            optimalesNetz.clear();
            return false;
        }
        return true;
    }

    public List<TelVerbindung> getOptTelNet() {
        return optimalesNetz;
    }

    public int getOptTelNetKosten() {
        return optimalesNetz.stream().mapToInt(i -> i.dist).sum();
    }

    public void drawOptTelNet(int xMax, int yMax) {
        StdDraw.clear();
        StdDraw.setXscale();
        StdDraw.setYscale();

        StdDraw.setCanvasSize(1000, 1000);

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLUE);
        for (TelKnoten k : knoten.keySet()) {
            StdDraw.point((double) k.x / xMax, (double) k.y / yMax);
        }

        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(Color.RED);
        optimalesNetz.forEach(i -> {
            StdDraw.line(i.from.x / (double) xMax, i.from.y / (double) yMax, i.from.x / (double) xMax, i.to.y / (double) yMax);
            StdDraw.line(i.from.x / (double) xMax, i.to.y / (double) yMax, i.to.x / (double) xMax, i.to.y / (double) yMax);
        });


        StdDraw.show(0);
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {

        Random rnd = new Random();

        for (int i = 0; i < n; i++) {

            int x = rnd.nextInt(xMax);
            int y = rnd.nextInt(yMax);

            if (!addTelKnoten(x, y))
                i--;
        }

        return;
    }

    public int size() {
        return knoten.size();
    }

    public static void main(String[] args) {
        /* Das Programm funktioniert nur im debug mode...
            in Eclipse etc. kann es normal ausgeführt werden.
            Daher die vermutung, dass IDEA falsch optimiert
         */
        doNetFromAbb3();
        doRandomNet();
    }

    private static void doRandomNet(){
        int size = 1000;
        TelNet rand = new TelNet(1000);
        rand.generateRandomTelNet(size,size,size);
        if (rand.computeOptTelNet()) {
            System.out.println("Abbildung 3 kosten: " + rand.getOptTelNetKosten());
            rand.getOptTelNet().forEach(System.out::println);
            rand.drawOptTelNet(size, size);
        } else
            System.out.println("computeing failed");
    }

    private static void doNetFromAbb3() {
        TelNet abb3 = new TelNet(50);
        abb3.addTelKnoten(1, 1);
        abb3.addTelKnoten(3, 1);
        abb3.addTelKnoten(4, 2);
        abb3.addTelKnoten(3, 4);
        abb3.addTelKnoten(7, 5);
        abb3.addTelKnoten(2, 6);
        abb3.addTelKnoten(4, 7);
        if (abb3.computeOptTelNet()) {
            System.out.println("Abbildung 3 kosten: " + abb3.getOptTelNetKosten());
            abb3.getOptTelNet().forEach(System.out::println);
            abb3.drawOptTelNet(7, 7);
        } else
            System.out.println("computeing failed");
    }
}
