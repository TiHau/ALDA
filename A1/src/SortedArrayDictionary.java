import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private final int DEF_CAP = 16;
    private int size;
    private Entry<K, V>[] data;

    public SortedArrayDictionary() {
        data = new Entry[DEF_CAP];
        size = 0;
    }


    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);
        if (i != -1) {
            V old = data[i].getValue();
            data[i].setValue(value);
            return old;
        } else {
            if (size == data.length) ensureCapacity(size * 2);
            int j = size - 1;
            while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = new Entry<K, V>(key, value);
            size++;
        }
        return null;
    }

    @Override
    public V search(K key) {
        int x = searchKey(key);
        if (x != -1) return data[x].getValue();
        return null;
    }

    private int searchKey(K key) {
        int li = 0;
        int re = size - 1;
        while (re >= li) {
            int mid = (li + re) / 2;
            int cmp = key.compareTo(data[mid].getKey());
            if (cmp < 0) {
                re = mid - 1;
            } else if (cmp > 0) {
                li = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            V old = data[i].getValue();
            System.arraycopy(data, i + 1, data, i, size - i);
            size--;
            return old;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new SortedArrayIterator();
    }

    private class SortedArrayIterator implements Iterator<Entry<K, V>> {
        private int pos = 0;

        @Override
        public boolean hasNext() {
            return size > pos;

        }

        @Override
        public Entry<K, V> next() {
            return data[pos++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void ensureCapacity(int newCap) {
        if (newCap < size) return;
        Entry<K, V>[] tmp = data;
        data = new Entry[newCap];
        System.arraycopy(tmp, 0, data, 0, size);
        return;
    }
}