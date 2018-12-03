import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    static private class Node<K, V> {
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;
        private Entry<K, V> data;
        private int height;

        private Node(Entry<K, V> e) {
            height = 0;
            data = e;
            left = null;
            right = null;
            parent = null;
        }
    }

    private Node<K, V> root;
    private int size;
    private V oldValue;

    public BinaryTreeDictionary() {
        root = null;
        size = 0;
        oldValue = null;
    }

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    private Node<K, V> insertR(K key, V value, Node<K, V> p) {
        if (p == null) {
            p = new Node(new Entry(key, value));
            p.parent = null;
            size++;
            oldValue = null;
        } else if (key.compareTo(p.data.getKey()) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null)
                p.left.parent = p;
        } else if (key.compareTo(p.data.getKey()) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null)
                p.right.parent = p;
        } else {
            oldValue = p.data.getValue();
            p.data.setValue(value);
        }
        p = balance(p);
        return p;
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K, V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.data.getKey()) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.data.getKey()) > 0)
            return searchR(key, p.right);
        else
            return p.data.getValue();
    }

    @Override
    public V remove(K key) {
        if (search(key) == null) return null;
        root = removeR(key, root);
        size--;
        return oldValue;
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {
        if (p == null) {
            oldValue = null;
        } else if (key.compareTo(p.data.getKey()) < 0)
            p.left = removeR(key, p.left);
        else if (key.compareTo(p.data.getKey()) > 0)
            p.right = removeR(key, p.right);
        else if (p.left == null || p.right == null) {
            oldValue = p.data.getValue();
            p = (p.left != null) ? p.left : p.right;
        } else { // falls p gelöscht wird und 2 Kinder hat
            MinEntry<K, V> min = new MinEntry<>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.data.getValue();
            p.data = new Entry<>(min.key, min.value);
        }
        p = balance(p);
        return p;
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }


    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        if (p.left == null) {
            min.key = p.data.getKey();
            min.value = p.data.getValue();

            if(p.right != null)
                p.right.parent = p.parent;
            p = p.right;
        } else {
            p.left = getRemMinR(p.left, min);
            if (p.left != null)
                p.left.parent = p;
        }
        p = balance(p);
        return p;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BinaryTreeIterator();
    }

    private class BinaryTreeIterator implements Iterator<Entry<K, V>> {
        Node<K, V> next = null;
        public BinaryTreeIterator() {
            if (root != null) {
                next = leftMostDescendant(root);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Entry<K, V> next() {
            if(!hasNext()) throw new NoSuchElementException();
            Entry<K,V> r = next.data;
            if (next.right != null)
                next = leftMostDescendant(next.right);
            else
                next = parentOfLeftMostAncestor(next);
            return r;
        }
    }

    private Node<K, V> leftMostDescendant(Node<K, V> p) {
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K, V> parentOfLeftMostAncestor(Node<K, V> p) {
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent;
    }

    public void prettyPrint() {
        prettyPrintR(root, 0);
    }

    private void prettyPrintR(Node p, int t) {
        if (t != 0) {
            System.out.print(tab(t) + "|___");
        }
        if (p != null) {
            if (p.parent != null)
            System.out.println(p.data.getKey() +"," + p.parent.data.getKey());
            else
                System.out.println(p.data.getKey());

            if (p.left == null && p.right == null) {
                return; // falls keine p Kinder hat
            }
            prettyPrintR(p.left, ++t);
            --t;
            prettyPrintR(p.right, ++t);
            --t;
        } else {
            System.out.println("#");
        }
    }

    public static String tab(int t) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < t; i++) {
            s.append("    ");
        }
        return s.toString();
    }

    private int getHeight(Node<K, V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    private int getBalance(Node<K, V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }

    private Node<K, V> balance(Node<K, V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        Node<K, V> oldParent = p.parent;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else
                p = rotateLeftRight(p);
        } else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else
                p = rotateRightLeft(p);
        }
        p.parent = oldParent; // fix parent
        return p;
    }

    private Node<K, V> rotateRight(Node<K, V> p) {
        Node<K, V> q = p.left;
        p.left = q.right;
        if (p.left != null)
            p.left.parent = p;
        q.right = p;
        p.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K, V> rotateLeft(Node<K, V> p) {
        Node<K, V> q = p.right;
        p.right = q.left;
        if (p.right != null)
            p.right.parent = p;
        q.left = p;
        p.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K, V> rotateLeftRight(Node<K, V> p) {
        p.left = rotateLeft(p.left);
        p.left.parent = p;
        return rotateRight(p);
    }

    private Node<K, V> rotateRightLeft(Node<K, V> p) {
        p.right = rotateRight(p.right);
        p.right.parent = p;
        return rotateLeft(p);
    }
}