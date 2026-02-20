package org.motadata.common.factory.map;

/**
 * Enum representing different types of map implementations available.
 *
 * @author prit.thakkar@motadata.com
 */
public enum MapType {
    /** Generic bucket hash map. */
    GENERIC,
    /** Treeified bucket hash map for better worst-case performance. */
    TREEIFIED,
    /** Linked hash map that maintains insertion order. */
    LINKED
}
