public class TelVerbindung implements Comparable<TelVerbindung> {
    public final TelKnoten from;
    public final TelKnoten to;
    public final int dist;

    TelVerbindung(TelKnoten from, TelKnoten to) {
        this.from = from;
        this.to = to;
        this.dist = Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    @Override
    public int compareTo(TelVerbindung other) {
        if(this.dist>other.dist) return 1;
        else if(this.dist == other.dist) {
            return 0;
        } else
            return -1;
    }

    @Override
    public String toString() {
        return "Verbindung von " + from + " nach " + to + " dist: " + dist;
    }
}
