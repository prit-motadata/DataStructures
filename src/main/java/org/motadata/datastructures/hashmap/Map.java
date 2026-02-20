package org.motadata.datastructures.hashmap;

import java.util.Collection;
import java.util.Set;

/**
 * Minimal map abstraction used by custom hash map implementations in this project.
 *
 * @param <K> key type
 * @param <V> value type
 * @author prit.thakkar@motadata.com
 */
public abstract class Map<K, V> {

    /**
     * Associates the given value with the specified key in this map.
     *
     * @param key   non-null key
     * @param value non-null value
     * @return {@code true} if a new key was added, {@code false} if the value for an existing key was updated
     */
    public abstract boolean put(K key, V value);

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if none.
     *
     * @param key key whose associated value is to be returned
     * @return value mapped to {@code key}, or {@code null} if no mapping exists
     */
    public abstract V get(K key);

    /**
     * Removes the mapping for a key from this map if present.
     *
     * @param key key whose mapping is to be removed
     * @return {@code true} if a mapping was removed, {@code false} otherwise
     */
    public abstract boolean remove(K key);

    /**
     * Returns a set view of the keys contained in this map.
     *
     * @return set of keys
     */
    public abstract Set<K> keySet();

    /**
     * Returns a collection view of the values contained in this map.
     *
     * @return collection of values
     */
    public abstract Collection<V> values();

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return current size of the map
     */
    public abstract int size();

    /**
     * Returns whether a mapping exists for the given key.
     *
     * @param key key to test
     * @return {@code true} if {@link #get(Object)} returns a non-null value, {@code false} otherwise
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Computes a non-negative hash bucket index for the given key and table size.
     *
     * @param key  key to hash
     * @param size size of the underlying bucket array
     * @return index within \([0, size)\) for the given key
     */
    protected int hash(K key, int size) {
        return (key.hashCode() & Integer.MAX_VALUE) % size;
    }
}
