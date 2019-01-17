public class UnionFind {
    int p[];

    public UnionFind(int n) {
        p = new int[n];

        for (int i = 0; i < p.length; i++)
            p[i] = -1;
    }

    public void union(int s1, int s2) throws java.lang.IllegalArgumentException {
        if (p[s1] >= 0 || p[s2] >= 0) return;
        if (s1 == s2) return;

        if (-p[s1] < -p[s2]) p[s1] = s2;
        else {
            if (-p[s1] == -p[s2]) p[s1]--;
            p[s2] = s1;
        }
    }

    int find(int e) {
        if (p[e] <= -1) return e;
        else return find(p[e]);
    }

    public int size() {
        int size = 0;

        for (int aP : p) {
            if (aP < 0) {
                size++;
            }
        }
        return size;
    }
}
