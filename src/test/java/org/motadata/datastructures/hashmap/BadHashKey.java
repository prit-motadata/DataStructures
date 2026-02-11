package org.motadata.datastructures.hashmap;

public class BadHashKey implements Comparable<BadHashKey> {
    private final int value;

    BadHashKey(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return 1; // force collision into same bucket
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BadHashKey other)) return false;
        return this.value == other.value;
    }

    @Override
    public int compareTo(BadHashKey o) {
        return Integer.compare(this.value, o.value);
    }
}

