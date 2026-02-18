package org.motadata.datastructures.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowCircularQueueTest {

    // ---------- Constructor Tests ----------

    @Test
    void testConstructorValid() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertEquals(5, buffer.capacity());
        assertEquals(0, buffer.size());
        assertEquals(100, buffer.getBaseSequence());
    }

    @Test
    void testConstructorInvalidWindowSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new SlidingWindowCircularQueue<>(0, 0));
    }

    // ---------- Add Tests ----------

    @Test
    void testAddWithinWindow() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertTrue(buffer.add(100, "A"));
        assertEquals(1, buffer.size());
    }

    @Test
    void testAddDuplicateDoesNotIncreaseSize() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        buffer.add(100, "A");
        buffer.add(100, "A-again");

        assertEquals(1, buffer.size());
    }

    @Test
    void testAddOldPacketReturnsFalse() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertFalse(buffer.add(99, "OLD"));
        assertEquals(0, buffer.size());
    }

    @Test
    void testAddOutsideUpperWindowReturnsFalse() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertFalse(buffer.add(105, "OUTSIDE")); // base+windowSize
        assertEquals(0, buffer.size());
    }

    @Test
    void testAddUpperBoundaryMinusOne() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertTrue(buffer.add(104, "VALID")); // last valid slot
        assertEquals(1, buffer.size());
    }

    // ---------- Poll Tests ----------

    @Test
    void testPollWhenEmpty() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        assertNull(buffer.poll());
    }

    @Test
    void testPollWhenNextPacketMissing() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        buffer.add(101, "B"); // missing 100

        assertNull(buffer.poll());
        assertEquals(1, buffer.size());
    }

    @Test
    void testPollInOrder() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(100, 5);

        buffer.add(100, "A");
        buffer.add(102, "C");
        buffer.add(101, "B");

        assertEquals("A", buffer.poll());
        assertEquals(101, buffer.getBaseSequence());

        assertEquals("B", buffer.poll());
        assertEquals("C", buffer.poll());

        assertNull(buffer.poll());
        assertEquals(0, buffer.size());
    }

    @Test
    void testHeadIndexWrapAround() {
        SlidingWindowCircularQueue<String> buffer =
                new SlidingWindowCircularQueue<>(1, 3);

        buffer.add(1, "A");
        buffer.add(2, "B");
        buffer.add(3, "C");

        assertEquals("A", buffer.poll());
        assertEquals("B", buffer.poll());
        assertEquals("C", buffer.poll());

        // headIndex should have wrapped internally
        assertEquals(4, buffer.getBaseSequence());
        assertEquals(0, buffer.size());
    }
}
