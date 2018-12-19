public class UnionFind {
    int p[];

    public UnionFind(int n) {
        p = new int[n];

        for (int i = 0; i < p.length; i++)
            p[i] = -1;
    }

    public void union (int s1, int s2) throws java.lang.IllegalArgumentException {
        if (p[s1]  >= 0 || p[s2]  >= 0) { //s1 und s2 muessen die Wurzel sein
            return;
        }
        if (s1 == s2) { //Falls s1 und s2 dieselbe Menge ist, dann mache nichts.
            return;
        }

        // Höhe von s1 < Höhe von s2
        if (-p[s1] < -p[s2]) {
            p[s1] = s2;
        } else { // Höhe von s1 erhöht sich um 1
            if (-p[s1] == -p[s2]) {
                p[s1]--;
            }
            p[s2] = s1;
        }
    }

    int find(int e) {
        if (p[e] <= -1) { //e ist Wurzel
            return e;
        } else {
            return find(p[e]);
        }
    }

    public int size() {
        int size = 0;

        for (int i = 0; i < p.length; i++) {
            if (p[i] <= 0) {
                size++;
            }
        }
        return size;
    }
}
