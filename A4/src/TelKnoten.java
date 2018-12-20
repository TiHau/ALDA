public class TelKnoten implements Comparable<TelKnoten> {
    public final int x;
    public final int y;

    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(TelKnoten other) {
        if (this.x == other.x && this.y == other.y)
            return 0;
        return -1;
    }

    @Override
    public String toString() {
        return "Knoten("+x+"|"+y+")";
    }
}
