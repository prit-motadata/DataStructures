package org.motadata.datastructures.hashmap;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class TreeifiedBucketHashMap<K extends Comparable<K>, V> extends Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int TREEIFY_THRESHOLD = 8;

    private final Bucket<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public TreeifiedBucketHashMap() {
        buckets = new Bucket[DEFAULT_CAPACITY];
    }

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

    @Override
    public V get(K key) {
        int index = hash(key, buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        return buckets[index].get(key);
    }

    @Override
    public boolean delete(K key) {
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

    @Override
    public int size() {
        return size;
    }

    // ---------------- Bucket ----------------

    private static class Bucket<K extends Comparable<K>, V> {

        private Entry<K, V> head;
        private TreeMap<K, V> tree;
        private int count;

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

        boolean remove(K key) {
            if (tree != null) {
                return tree.remove(key) != null;
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
