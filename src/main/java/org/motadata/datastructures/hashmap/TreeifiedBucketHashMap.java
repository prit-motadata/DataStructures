package org.motadata.datastructures.hashmap;

import java.util.*;

/**
 * Hash map that upgrades heavily populated buckets to tree-based storage for faster lookups.
 *
 * <p>Each bucket starts as a simple linked list and is "treeified" into a {@link java.util.TreeMap}
 * when the number of entries exceeds a threshold.</p>
 *
 * @param <K> comparable key type
 * @param <V> value type
 * @author prit.thakkar@motadata.com
 */
public class TreeifiedBucketHashMap<K extends Comparable<K>, V> extends Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int TREEIFY_THRESHOLD = 8;

    private final Bucket<K, V>[] buckets;
    private int size;

    /**
     * Creates an empty map with default bucket capacity.
     */
    @SuppressWarnings("unchecked")
    public TreeifiedBucketHashMap() {
        buckets = new Bucket[DEFAULT_CAPACITY];
    }

    /**
     * Associates the specified value with the given key, treeifying buckets as needed.
     *
     * @param key   comparable key
     * @param value value to associate with the key
     * @return {@code true} if a new entry was created, {@code false} if an existing value was updated
     */
    @Override
    public boolean put(K key, V value) {
        int index = hash(key, buckets.length);

        if (buckets[index] == null) {
            buckets[index] = new Bucket<>();
        }

        boolean isNew = buckets[index].put(key, value);
        if (isNew) {
            size++;
        }

        return isNew;
    }

    /**
     * Retrieves the value mapped to the given key, or {@code null} if none exists.
     *
     * @param key key whose associated value is to be returned
     * @return mapped value or {@code null} if none
     */
    @Override
    public V get(K key) {
        int index = hash(key, buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        return buckets[index].get(key);
    }

    /**
     * Removes the mapping for the specified key, if present.
     *
     * @param key key whose mapping should be removed
     * @return {@code true} if an entry was removed, {@code false} otherwise
     */
    @Override
    public boolean remove(K key) {
        int index = hash(key, buckets.length);
        if (buckets[index] == null) {
            return false;
        }

        boolean removed = buckets[index].remove(key);
        if (removed) {
            size--;
        }
        return removed;
    }

    /**
     * Returns all keys across every bucket.
     *
     * @return set of keys currently stored in the map
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();

        for (Bucket<K, V> bucket : buckets) {
            if (bucket != null) {
                keys.addAll(bucket.keys());
            }
        }
        return keys;
    }

    /**
     * Returns all values across every bucket.
     *
     * @return collection of values currently stored in the map
     */
    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();

        for (Bucket<K, V> bucket : buckets) {
            if (bucket != null) {
                values.addAll(bucket.values());
            }
        }

        return values;
    }

    /**
     * Returns the number of key-value mappings stored in the map.
     *
     * @return current size
     */
    @Override
    public int size() {
        return size;
    }

    // ---------------- Bucket ----------------

    /**
     * Bucket that stores entries either in a linked list or in a {@link TreeMap} after treeification.
     *
     * @param <K> comparable key type
     * @param <V> value type
     */
    private static class Bucket<K extends Comparable<K>, V> {

        private Entry<K, V> head;
        private TreeMap<K, V> tree;
        private int count;

        /**
         * Inserts or updates an entry in this bucket, treeifying if the threshold is reached.
         *
         * @param key   key to insert or update
         * @param value value to associate with the key
         * @return {@code true} if a new entry was created, {@code false} if an existing value was updated
         */
        boolean put(K key, V value) {
            if (tree != null) {
                return tree.put(key, value) == null;
            }

            Entry<K, V> current = head;
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return false;
                }
                current = current.next;
            }

            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = head;
            head = newEntry;
            count++;

            if (count >= TREEIFY_THRESHOLD) {
                treeify();
            }

            return true;
        }

        /**
         * Returns the value associated with the given key in this bucket, if present.
         *
         * @param key key to search
         * @return value mapped to the key or {@code null} if none
         */
        V get(K key) {
            if (tree != null) {
                return tree.get(key);
            }

            Entry<K, V> current = head;
            while (current != null) {
                if (current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        /**
         * Removes the entry for the given key from this bucket.
         *
         * @param key key whose mapping should be removed
         * @return {@code true} if an entry was removed, {@code false} otherwise
         */
        boolean remove(K key) {
            if (tree != null) {
                boolean removed = tree.remove(key) != null;
                if (removed) count--;
                return removed;
            }

            Entry<K, V> current = head;
            Entry<K, V> prev = null;

            while (current != null) {
                if (current.key.equals(key)) {
                    if (prev == null) {
                        head = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    count--;
                    return true;
                }
                prev = current;
                current = current.next;
            }
            return false;
        }

        /**
         * Returns all keys contained in this bucket.
         *
         * @return set of keys
         */
        Set<K> keys() {
            Set<K> result = new HashSet<>();

            if (tree != null) {
                result.addAll(tree.keySet());
            } else {
                Entry<K, V> current = head;
                while (current != null) {
                    result.add(current.key);
                    current = current.next;
                }
            }
            return result;
        }

        /**
         * Returns all values contained in this bucket.
         *
         * @return collection of values
         */
        Collection<V> values() {
            Collection<V> result = new ArrayList<>();

            if (tree != null) {
                result.addAll(tree.values());
            } else {
                Entry<K, V> current = head;
                while (current != null) {
                    result.add(current.value);
                    current = current.next;
                }
            }

            return result;
        }

        /**
         * Converts the internal storage from a linked list to a {@link TreeMap} for faster lookups.
         */
        private void treeify() {
            tree = new TreeMap<>();
            Entry<K, V> current = head;

            while (current != null) {
                tree.put(current.key, current.value);
                current = current.next;
            }

            head = null; // free linked list
        }
    }

    // ---------------- Entry ----------------

    /**
     * Entry node used by the bucket's linked-list representation.
     *
     * @param <K> key type
     * @param <V> value type
     */
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
