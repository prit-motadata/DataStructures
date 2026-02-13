package org.motadata.datastructures.heap;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueBinaryHeapTest {

    // ---------- Constructor Tests ----------

    @Test
    void testDefaultConstructor() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    void testComparatorConstructor() {
        PriorityQueueBinaryHeap<Integer> heap =
                new PriorityQueueBinaryHeap<>(Comparator.reverseOrder());

        heap.offer(1);
        heap.offer(5);
        heap.offer(3);

        assertEquals(5, heap.poll()); // max heap behavior
    }

    @Test
    void testCapacityConstructorInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new PriorityQueueBinaryHeap<>(0, null));
    }

    // ---------- Offer Tests ----------

    @Test
    void testOfferAndPeek() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();

        heap.offer(5);
        heap.offer(1);
        heap.offer(3);

        assertEquals(1, heap.peek()); // min heap
        assertEquals(3, heap.size());
    }

    @Test
    void testOfferNullThrowsException() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();
        assertThrows(NullPointerException.class, () -> heap.offer(null));
    }

    @Test
    void testEnsureCapacityResize() {
        PriorityQueueBinaryHeap<Integer> heap =
                new PriorityQueueBinaryHeap<>(2, null);

        heap.offer(3);
        heap.offer(2);
        heap.offer(1); // triggers resize

        assertEquals(3, heap.size());
        assertEquals(1, heap.peek());
    }

    // ---------- Poll Tests ----------

    @Test
    void testPollEmpty() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();
        assertNull(heap.poll());
    }

    @Test
    void testPollMaintainsHeapOrder() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();

        heap.offer(4);
        heap.offer(2);
        heap.offer(8);
        heap.offer(1);
        heap.offer(5);

        assertEquals(1, heap.poll());
        assertEquals(2, heap.poll());
        assertEquals(4, heap.poll());
        assertEquals(5, heap.poll());
        assertEquals(8, heap.poll());
        assertNull(heap.poll());
    }

    // ---------- SiftDown Right Child Path ----------

    @Test
    void testSiftDownRightChildSmaller() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();

        heap.offer(10);
        heap.offer(5);
        heap.offer(1); // right child smaller than left

        assertEquals(1, heap.poll());
    }

    // ---------- Peek Empty ----------

    @Test
    void testPeekEmpty() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();
        assertNull(heap.peek());
    }

    // ---------- Natural Ordering Compare Path ----------

    @Test
    void testNaturalOrderingPath() {
        PriorityQueueBinaryHeap<String> heap = new PriorityQueueBinaryHeap<>();

        heap.offer("c");
        heap.offer("a");
        heap.offer("b");

        assertEquals("a", heap.poll());
    }

    // ---------- Comparator Path ----------

    @Test
    void testCustomComparatorPath() {
        Comparator<Integer> maxComparator = (a, b) -> b - a;

        PriorityQueueBinaryHeap<Integer> heap =
                new PriorityQueueBinaryHeap<>(maxComparator);

        heap.offer(1);
        heap.offer(3);
        heap.offer(2);

        assertEquals(3, heap.poll());
    }

    // ---------- isEmpty & size ----------

    @Test
    void testIsEmptyAndSize() {
        PriorityQueueBinaryHeap<Integer> heap = new PriorityQueueBinaryHeap<>();

        assertTrue(heap.isEmpty());

        heap.offer(1);
        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());

        heap.poll();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }
}
