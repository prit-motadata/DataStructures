package org.motadata.datastructures.hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Hash map implementation that stores entries in linked lists within each bucket.
 *
 * <p>This implementation resizes itself when the load factor threshold is exceeded.</p>
 *
 * @param <K> key type
 * @param <V> value type
 * @author prit.thakkar@motadata.com
 */
public class GenericBucketHashMap<K, V> extends Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;
    private int threshold;

    /**
     * Creates an empty hash map with default capacity and load factor.
     */
    @SuppressWarnings("unchecked")
    public GenericBucketHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    /**
     * Associates the specified value with the given key using separate chaining.
     *
     * @param key   non-null key
     * @param value non-null value
     * @return {@code true} if a new entry was inserted, {@code false} if an existing value was updated
     */
    @Override
    public boolean put(K key, V value) {
        int index = hash(key, buckets.length);

        Entry<K, V> head = buckets[index];

        // Update if key already exists
        Entry<K, V> current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return false; // not a new entry
            }
            current = current.next;
        }

        // Insert new entry at head
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets[index] = newEntry;
        size++;

        // Resize check
        if (size >= threshold) {
            resize();
        }

        return true;
    }

    /**
     * Retrieves the value for the given key or {@code null} if no mapping exists.
     *
     * @param key key whose associated value is to be returned
     * @return mapped value or {@code null} if none
     */
    @Override
    public V get(K key) {
        int index = hash(key, buckets.length);

        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
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

        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    /**
     * Returns a set of all keys stored in this map.
     *
     * @return set containing all keys
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();

        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }

        return keys;
    }

    /**
     * Returns a collection of all values stored in this map.
     *
     * @return collection containing all values
     */
    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();

        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }

        return values;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return current size
     */
    @Override
    public int size() {
        return size;
    }

    // ================= RESIZE =================

    /**
     * Doubles the bucket array size and rehashes all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = buckets.length * 2;
        Entry<K, V>[] oldBuckets = buckets;

        buckets = new Entry[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        // Rehash all entries
        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                Entry<K, V> next = current.next;

                int newIndex = hash(current.key, newCapacity);

                // Insert at head in new table
                current.next = buckets[newIndex];
                buckets[newIndex] = current;

                current = next;
            }
        }
    }

    // ===== Entry Node =====
    /**
     * Node representing an entry in a bucket's linked list.
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
