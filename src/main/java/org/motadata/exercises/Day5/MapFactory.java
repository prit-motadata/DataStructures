package org.motadata.exercises.Day5;

import org.motadata.datastructures.hashmap.GenericBucketHashMap;
import org.motadata.datastructures.hashmap.Map;
import org.motadata.datastructures.hashmap.TreeifiedBucketHashMap;

public final class MapFactory {

    private MapFactory() {}

    public static <K extends Comparable<K>, V> Map<K, V> createMap(MapType type) {
        return switch (type) {
            case TREEIFIED -> new TreeifiedBucketHashMap<>();
            case GENERIC -> new GenericBucketHashMap<>();
        };
    }

    public static <K, V> Map<K, V> createDefault() {
        return new GenericBucketHashMap<>();
    }
}