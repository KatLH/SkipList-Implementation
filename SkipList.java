import java.util.NoSuchElementException;
import java.util.NoSuchElementException;
import java.util.Random;

javaclass QuadNode<K extends Comparable<K>,V> {
    private QuadNode<K,V>
            up,
            down,
            next,
            previous;
    private K key;
    private V value;
    private int level;

    public QuadNode(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
    }

    public void insert(K key, V value, int level, QuadNode<K,V> parent) {
        if (this.level <= level && (next == null || next.getKey().compareTo(key) > 0)) {
            QuadNode<K, V> newNode = new QuadNode<K, V>(key, value, this.level);

            if (next != null) {
                next.setPrevious(newNode);
                newNode.setNext(next);
            }

            next = newNode;
            newNode.setPrevious(this);

            if (parent != null) {
                newNode.setUp(parent);
                parent.setDown(newNode);
            }

            if (down != null) {
                down.insert(key, value, level, newNode);
            }
        } else if (next != null && next.getKey().compareTo(key) < 0) {
            next.insert(key, value, level, parent);
        } else if (next != null && next.getKey().compareTo(key) == 0) {
            throw new IllegalArgumentException("Duplicate key is not allowed!");
        } else if (down != null) {
            down.insert(key, value, level, parent);
        }
    }

    public QuadNode<K, V> find(K key) {
        if (next != null) {
            int compareResult = next.getKey().compareTo(key);

            if (compareResult == 0) {
                return next;
            } else if (compareResult < 0) {
                return next.find(key);
            } else if (down != null) {
                return down.find(key);
            } else {
                throw new NoSuchElementException();
            }
        } else if (down != null) {
            return down.find(key);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        QuadNode<K, V> bottom = this;

        while (bottom.getDown() != null) {
            bottom = bottom.getDown();
        }

        for (QuadNode<K, V> node = bottom; node != null; node = node.getUp()) {
            sb.append('[').append((node.getKey() == null) ? "H" : node.getKey().toString()).append(']');
        }

        if (bottom.next != null) {
            sb.append('\n').append(bottom.next.toString());
        }

        return sb.toString();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setPrevious(QuadNode qn) {
        previous = qn;
    }

    public void setNext(QuadNode qn) {
        next = qn;
    }

    public void setDown(QuadNode qn) {
        down = qn;
    }

    public void setUp(QuadNode qn) {
        up = qn;
    }

    public QuadNode<K,V> getUp() {
        return up;
    }

    public QuadNode<K,V> getDown() {
        return down;
    }

    public QuadNode<K,V> getPrevious() {
        return previous;
    }

    public QuadNode<K,V> getNext() {
        return next;
    }

    public int getLevel() {
        return level;
    }
}

public class SkipList<K extends Comparable<K>,V> {
    private QuadNode<K, V> head = new QuadNode<K, V>(null, null, 0);
    final static Random random = new Random();

    public void insert(K key, V value) {
        int level = 0;

        while (random.nextBoolean()) {
            level++;
        }

        while (head.getLevel() < level) {
            QuadNode<K, V> newHead = new QuadNode<K, V>(null, null, head.getLevel() + 1);
            head.setUp(newHead);
            newHead.setDown(head);
            head = newHead;
        }

        head.insert(key, value, level, null);
    }

    public V find(K key) {
        return head.find(key).getValue();
    }

    public void delete(K key) {
        for (QuadNode<K,V> node = head.find(key); node != null; node = node.getDown()) {
            node.getPrevious().setNext(node.getNext());
            if (node.getNext() != null) {
                node.getNext().setPrevious(node.getPrevious());
            }
        }

        while (head.getNext() == null) {
            head = head.getDown();
            head.setUp(null);
        }
    }

    @Override
    public String toString() {
        return head.toString();
    }

    public static void main(String[] args) {
        SkipList<Integer, String> sl = new SkipList<Integer, String>();
        sl.insert(3, "tre");
        sl.insert(6, "sex");
        sl.insert(2, "två");
        sl.insert(5, "fem");
        sl.insert(1, "ett");
        try {
            sl.insert(1, "ett");
            throw new IllegalStateException("Duplicates are not allowed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Yes, 1 should not be allowed again.");
        }

        System.out.println(sl);
        System.out.println("-------");
        System.out.println(sl.find(3).equals("tre"));
        System.out.println(sl.find(6).equals("sex"));
        System.out.println(sl.find(2).equals("två"));
        System.out.println(sl.find(5).equals("fem"));
        System.out.println(sl.find(1).equals("ett"));

        sl.delete(6);
        System.out.println(sl);
        System.out.println("-------");

        sl.delete(3);
        System.out.println(sl);
        System.out.println("-------");

        try {
            sl.find(3);
            throw new IllegalStateException("Nooo!");
        } catch (NoSuchElementException e) {
            System.out.println("Yes, 3 should not be found");
        }
    }
}