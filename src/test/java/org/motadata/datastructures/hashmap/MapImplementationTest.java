package org.motadata.datastructures.hashmap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.motadata.common.factory.MapFactory;
import org.motadata.common.factory.MapType;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MapImplementationTest {

    private Map<String, String> createMap(MapType type) {
        return MapFactory.createMap(type);
    }

    // -------- PUT --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldInsertNewKey(MapType type) {
        Map<String, String> map = createMap(type);

        boolean inserted = map.put("a", "1");

        assertTrue(inserted);
        assertEquals(1, map.size());
        assertEquals("1", map.get("a"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldUpdateExistingKey(MapType type) {
        Map<String, String> map = createMap(type);

        map.put("a", "1");
        boolean inserted = map.put("a", "2");

        assertFalse(inserted); // update, not new
        assertEquals(1, map.size());
        assertEquals("2", map.get("a"));
    }

    // -------- GET --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnNullForMissingKey(MapType type) {
        Map<String, String> map = createMap(type);

        assertNull(map.get("missing"));
    }

    // -------- REMOVE --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldRemoveExistingKey(MapType type) {
        Map<String, String> map = createMap(type);

        map.put("a", "1");

        boolean removed = map.remove("a");

        assertTrue(removed);
        assertEquals(0, map.size());
        assertNull(map.get("a"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnFalseWhenRemovingMissingKey(MapType type) {
        Map<String, String> map = createMap(type);

        assertFalse(map.remove("ghost"));
    }

    // -------- KEYSET --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnAllKeys(MapType type) {
        Map<String, String> map = createMap(type);

        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        Set<String> keys = map.keySet();

        assertEquals(3, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
        assertTrue(keys.contains("c"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void keySetShouldBeEmptyWhenMapEmpty(MapType type) {
        Map<String, String> map = createMap(type);

        Set<String> keys = map.keySet();

        assertNotNull(keys);
        assertTrue(keys.isEmpty());
    }

    // -------- VALUES --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnAllValues(MapType type) {
        Map<String, String> map = createMap(type);

        map.put("a", "1");
        map.put("b", "2");

        Collection<String> values = map.values();

        assertEquals(2, values.size());
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void valuesShouldBeEmptyWhenMapEmpty(MapType type) {
        Map<String, String> map = createMap(type);

        Collection<String> values = map.values();

        assertNotNull(values);
        assertTrue(values.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldRemoveMiddleNodeFromBucket(MapType type) {
        Map<BadHashKey, String> map = MapFactory.createMap(type);

        BadHashKey k1 = new BadHashKey(1);
        BadHashKey k2 = new BadHashKey(2);
        BadHashKey k3 = new BadHashKey(3);

        // All go into same bucket
        map.put(k1, "A");
        map.put(k2, "B");
        map.put(k3, "C");

        assertEquals(3, map.size());

        // Remove middle node (NOT head)
        assertTrue(map.remove(k2));

        assertEquals(2, map.size());
        assertNull(map.get(k2));
        assertEquals("A", map.get(k1));
        assertEquals("C", map.get(k3));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldHandleLargeNumberOfInsertionsAndResize(MapType type) {
        Map<Integer, String> map = MapFactory.createMap(type);

        // Insert enough elements to force multiple resizes
        for (int i = 0; i < 100; i++) {
            map.put(i, "val" + i);
        }

        assertEquals(100, map.size());

        // Verify all elements exist
        for (int i = 0; i < 100; i++) {
            assertEquals("val" + i, map.get(i));
        }
    }

    // -------- DEFAULT FACTORY --------

    @Test
    void shouldCreateGenericMapByDefault() {
        Map<String, String> map = MapFactory.createDefault();

        map.put("a", "1");

        assertEquals("1", map.get("a"));
        assertEquals(1, map.size());
    }

    // -------- Treeified Bucket HashMap Tests --------

    @ParameterizedTest
    @EnumSource(value = MapType.class, names = "TREEIFIED")
    void shouldTriggerTreeify(MapType type) {
        Map<BadHashKey, String> map = MapFactory.createMap(type);

        // Insert more than TREEIFY_THRESHOLD (8)
        for (int i = 0; i < 10; i++) {
            map.put(new BadHashKey(i), "val" + i);
        }

        assertEquals(10, map.size());

        // Verify retrieval still works (now via TreeMap)
        for (int i = 0; i < 10; i++) {
            assertEquals("val" + i, map.get(new BadHashKey(i)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = MapType.class, names = "TREEIFIED")
    void shouldCoverTreeRemoveBranches(MapType type) {
        Map<BadHashKey, String> map = MapFactory.createMap(type);

        // Force treeify (10 > threshold 8)
        for (int i = 0; i < 10; i++) {
            map.put(new BadHashKey(i), "val" + i);
        }

        // Remove existing (removed = true)
        assertTrue(map.remove(new BadHashKey(5)));

        // Remove non-existing (removed = false)
        assertFalse(map.remove(new BadHashKey(100)));
    }
}
