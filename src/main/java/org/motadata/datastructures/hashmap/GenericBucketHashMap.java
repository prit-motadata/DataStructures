package org.motadata.datastructures.hashmap;

import java.util.HashSet;
import java.util.Set;

public class GenericBucketHashMap<K, V> extends Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private final Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public GenericBucketHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

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

        // Insert new entry at head (O(1))
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets[index] = newEntry;
        size++;

        return true;
    }

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

    @Override
    public boolean delete(K key) {
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

    @Override
    public int size() {
        return size;
    }

    // ===== Entry Node =====
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
