package org.motadata.datastructures.hashmap;

import java.util.*;

/**
 * Hash map implementation that maintains insertion or access order using a linked list of entries.
 *
 * <p>The iteration order is either by insertion (default) or by access when {@code accessOrder} is enabled.</p>
 *
 * @param <K> key type
 * @param <V> value type
 * @author prit.thakkar@motadata.com
 */
public class CustomLinkedHashMap<K, V> extends Map<K, V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private Node<K, V> head;
    private Node<K, V> tail;

    private final boolean accessOrder;

    /**
     * Creates a new map with default capacity, insertion-order iteration.
     */
    public CustomLinkedHashMap() {
        this(DEFAULT_CAPACITY, false);
    }

    /**
     * Creates a new map with the given capacity and ordering mode.
     *
     * @param capacity    initial bucket capacity
     * @param accessOrder {@code true} for access-order iteration, {@code false} for insertion-order
     */
    @SuppressWarnings("unchecked")
    public CustomLinkedHashMap(int capacity, boolean accessOrder) {
        this.table = new Node[capacity];
        this.threshold = (int) (capacity * LOAD_FACTOR);
        this.accessOrder = accessOrder;
    }

    // -------- PUT --------

    /**
     * Associates the specified value with the given key and appends the entry to the linked order list.
     *
     * @param key   non-null key
     * @param value non-null value
     * @return {@code true} if a new key was added, {@code false} if an existing key was updated
     */
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

    /**
     * Returns the value for the specified key and optionally moves the entry to the end if access order is enabled.
     *
     * @param key key whose associated value is to be returned
     * @return mapped value or {@code null} if none
     */
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

    /**
     * Removes the mapping for the given key, updating both the bucket chain and the linked order list.
     *
     * @param key key whose mapping is to be removed
     * @return {@code true} if an entry was removed, {@code false} otherwise
     */
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

    /**
     * Returns the number of key-value mappings stored in this map.
     *
     * @return current size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns keys in their current iteration order.
     *
     * @return ordered set of keys
     */
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

    /**
     * Returns values in the same order as {@link #keySet()} iteration.
     *
     * @return ordered collection of values
     */
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

    /**
     * Locates the node corresponding to the given key in the bucket chain.
     *
     * @param key key to search for
     * @return matching node or {@code null} if not found
     */
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

    /**
     * Appends the given node to the end of the doubly-linked iteration list.
     *
     * @param node node to link
     */
    private void linkLast(Node<K, V> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    /**
     * Removes the given node from the doubly-linked iteration list.
     *
     * @param node node to unlink
     */
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

    /**
     * Moves an existing node to the end of the doubly-linked iteration list.
     *
     * @param node node to move
     */
    private void moveToEnd(Node<K, V> node) {
        if (node == tail) return;

        unlink(node);
        linkLast(node);
    }

    /**
     * Doubles the bucket table size and rehashes all nodes while preserving iteration order.
     */
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

    /**
     * Returns an iterator over keys in their iteration order.
     *
     * @return iterator over keys
     */
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

    /**
     * Internal node used for both bucket chains and the iteration linked list.
     *
     * @param <K> key type
     * @param <V> value type
     */
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
