package org.motadata.common.factory.map;

import org.motadata.datastructures.hashmap.CustomLinkedHashMap;
import org.motadata.datastructures.hashmap.GenericBucketHashMap;
import org.motadata.datastructures.hashmap.Map;
import org.motadata.datastructures.hashmap.TreeifiedBucketHashMap;

/**
 * Factory class for creating different types of Map implementations.
 *
 * @author prit.thakkar@motadata.com
 */
public final class MapFactory {

    private MapFactory() {}

    /**
     * Creates a map of the specified type.
     *
     * @param <K>  the type of keys maintained by this map (must be Comparable for
     *             TreeifiedBucketHashMap)
     * @param <V>  the type of mapped values
     * @param type the type of map implementation to create
     * @return a new Map instance
     */
    public static <K extends Comparable<K>, V> Map<K, V> createMap(MapType type) {
        return switch (type) {
            case TREEIFIED -> new TreeifiedBucketHashMap<>();
            case LINKED -> new CustomLinkedHashMap<>();
            case GENERIC -> new GenericBucketHashMap<>();
        };
    }

    /**
     * Creates a default map implementation (GenericBucketHashMap).
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return a new default Map instance
     */
    public static <K, V> Map<K, V> createDefault() {
        return new GenericBucketHashMap<>();
    }
}