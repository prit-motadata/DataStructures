package org.motadata.datastructures.hashmap;

import java.util.*;

public class CustomLinkedHashMap<K, V> extends Map<K, V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private Node<K, V> head;
    private Node<K, V> tail;

    private final boolean accessOrder;

    public CustomLinkedHashMap() {
        this(DEFAULT_CAPACITY, false);
    }

    @SuppressWarnings("unchecked")
    public CustomLinkedHashMap(int capacity, boolean accessOrder) {
        this.table = new Node[capacity];
        this.threshold = (int) (capacity * LOAD_FACTOR);
        this.accessOrder = accessOrder;
    }

    // -------- PUT --------

    @Override
    public boolean put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("Null not supported");
        }

        int index = hash(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return false; // key already existed
            }
            current = current.bucketNext;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.bucketNext = table[index];
        table[index] = newNode;

        linkLast(newNode);

        size++;

        if (size >= threshold) {
            resize();
        }

        return true; // new key added
    }

    // -------- GET --------

    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) return null;

        if (accessOrder) {
            moveToEnd(node);
        }

        return node.value;
    }

    // -------- REMOVE --------

    @Override
    public boolean remove(K key) {
        int index = hash(key, table.length);

        Node<K, V> prev = null;
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {

                if (prev == null) {
                    table[index] = current.bucketNext;
                } else {
                    prev.bucketNext = current.bucketNext;
                }

                unlink(current);
                size--;
                return true;
            }

            prev = current;
            current = current.bucketNext;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new LinkedHashSet<>();

        Node<K, V> current = head;
        while (current != null) {
            keys.add(current.key);
            current = current.after;
        }

        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();

        Node<K, V> current = head;
        while (current != null) {
            values.add(current.value);
            current = current.after;
        }

        return values;
    }

    // -------- Internal --------

    private Node<K, V> getNode(K key) {
        int index = hash(key, table.length);

        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.bucketNext;
        }

        return null;
    }

    private void linkLast(Node<K, V> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlink(Node<K, V> node) {
        Node<K, V> before = node.before;
        Node<K, V> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
        }

        node.before = node.after = null;
    }

    private void moveToEnd(Node<K, V> node) {
        if (node == tail) return;

        unlink(node);
        linkLast(node);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);

        Node<K, V> current = head;

        while (current != null) {
            int index = hash(current.key, table.length);
            current.bucketNext = table[index];
            table[index] = current;
            current = current.after;
        }
    }

    // -------- Iterator --------

    @Override
    public Iterator<K> iterator() {
        return new Iterator<>() {
            Node<K, V> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                if (current == null)
                    throw new NoSuchElementException();

                K key = current.key;
                current = current.after;
                return key;
            }
        };
    }

    private static class Node<K, V> {
        final K key;
        V value;

        Node<K, V> bucketNext; // collision chain
        Node<K, V> before; // linked list
        Node<K, V> after;  // linked list

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
