package org.motadata.datastructures.hashmap;

import java.util.Collection;
import java.util.Set;

public abstract class Map<K, V> {

    public abstract boolean put(K key, V value);

    public abstract V get(K key);

    public abstract boolean remove(K key);

    public abstract Set<K> keySet();

    public abstract Collection<V> values();

    public abstract int size();

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    protected int hash(K key, int size) {
        return (key.hashCode() & Integer.MAX_VALUE) % size;
    }
}
