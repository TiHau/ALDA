public class TelVerbindung implements Comparable<TelVerbindung> {
    public final TelKnoten from;
    public final TelKnoten to;
    public final int dist;

    public TelVerbindung(TelKnoten from, TelKnoten to, int dist) {
        this.from = from;
        this.to = to;
        this.dist = dist;
    }

    TelVerbindung(TelKnoten from, TelKnoten to) {
        this.from = from;
        this.to = to;
        this.dist = Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    @Override
    public int compareTo(TelVerbindung other) {
        if (this.from.equals(other.from) && this.to.equals(other.to) && this.dist == other.dist)
            return 0;
        return -1;
    }
}
