import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K, V> implements Dictionary<K, V> {

    private LinkedList<Entry<K, V>>[] data;
    private int cap;
    private int size;
    private final int DEF_CAP = 3;

    public HashDictionary() {
        data = new LinkedList[DEF_CAP];
        size = 0;
        cap = DEF_CAP;
    }

    public HashDictionary(int cap) {
        data = new LinkedList[cap];
        size = 0;
        this.cap = cap;
    }

    @Override
    public V insert(K key, V value) {
        int hash = hash(key);
        if (size / cap >= 2) ensureCapacity();
        if (search(key) != null) {
            V old = null;
            for (Entry<K, V> e : data[hash]) {
                if (e.getKey().equals(key)) {
                    old = e.getValue();
                    e.setValue(value);
                }
            }
            return old;
        } else {
            if (data[hash] == null) data[hash] = new LinkedList<>(); // create new list
            data[hash].add(new Entry<>(key, value)); // add element
            size++;
            return null;
        }
    }

    @Override
    public V search(K key) {
        int hash = hash(key);
        if (data[hash] == null) return null;
        for (Entry<K, V> e : data[hash]) {
            if (e.getKey().equals(key)) return e.getValue();
        }
        return null;
    }

    @Override
    public V remove(K key) {
        V rem = null;
        if (search(key) != null) {
            int i = 0;
            for (Entry<K, V> e : data[hash(key)]) {
                if (e.getKey().equals(key)) {
                    rem = e.getValue();
                    data[hash(key)].remove(i);
                    size--;
                    break;
                }
                i++;
            }
        }
        return rem;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionaryIterator();
    }

    public int hash(K key) {
        int adr = key.hashCode();
        if (adr < 0) adr = -adr;
        return adr % cap;
    }

    public void ensureCapacity() {
        cap *= 2;
        while (!isPrime(cap)) cap++;

        LinkedList<Entry<K, V>>[] tmp = data;
        data = new LinkedList[cap];
        for (LinkedList<Entry<K, V>> l : tmp) {
            if (l == null) continue;
            for (Entry<K, V> e : l) {
                this.insert(e.getKey(), e.getValue());
            }
        }

        return;
    }

    private static boolean isPrime(int num) {
        if (num % 2 == 0) return false;
        for (int i = 3; i * i < num; i += 2)
            if (num % i == 0) return false;
        return true;
    }


    private class HashDictionaryIterator implements Iterator<Entry<K, V>> {

        private int state = 0;
        private int branchstate = 0;

        @Override
        public boolean hasNext() {
            return state < data.length - 1;
        }

        @Override
        public Entry<K, V> next() {
            Entry<K, V> ret = null;
            LinkedList<Entry<K, V>> l;
            while (ret == null) {
                if (data[state] != null) {
                    l = data[state];
                    if (branchstate < l.size()) {
                        ret = l.get(branchstate);
                        branchstate++;
                        return ret;
                    } else {
                        branchstate = 0;
                        state++;
                    }
                } else {
                    state++;
                }
            }
            return ret;
        }

    }

}
