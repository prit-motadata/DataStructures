package org.motadata.datastructures.heap;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Generic priority queue backed by a binary heap stored in an array.
 *
 * <p>Orders elements according to a provided {@link Comparator} or their natural ordering
 * if no comparator is supplied.</p>
 *
 * @param <E> element type stored in the queue
 * @author prit.thakkar@motadata.com
 */
public class PriorityQueueBinaryHeap<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    // -------- Constructors --------

    /**
     * Creates an empty priority queue with default capacity and natural element ordering.
     */
    public PriorityQueueBinaryHeap() {
        this(DEFAULT_CAPACITY, null);
    }

    /**
     * Creates an empty priority queue with default capacity and the given comparator.
     *
     * @param comparator comparator used to order elements, or {@code null} for natural ordering
     */
    public PriorityQueueBinaryHeap(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    /**
     * Creates an empty priority queue with the given capacity and comparator.
     *
     * @param capacity   initial backing array size
     * @param comparator comparator used to order elements, or {@code null} for natural ordering
     * @throws IllegalArgumentException if {@code capacity} is not positive
     */
    public PriorityQueueBinaryHeap(int capacity, Comparator<? super E> comparator) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.elements = new Object[capacity];
        this.comparator = comparator;
    }

    // -------- Public APIs --------

    /**
     * Inserts the specified element into this priority queue.
     *
     * @param element element to add (must not be {@code null})
     * @throws NullPointerException if {@code element} is {@code null}
     */
    public void offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity();
        elements[size] = element;
        siftUp(size);
        size++;
    }

    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if the queue is empty.
     *
     * @return smallest element according to the comparator or natural ordering, or {@code null} if empty
     * @see #peek()
     */
    public E poll() {
        if (isEmpty()) {
            return null;
        }

        E result = elementAt(0);
        E last = elementAt(size - 1);

        elements[0] = last;
        elements[size - 1] = null;
        size--;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns {@code null} if the queue is empty.
     *
     * @return smallest element in the queue, or {@code null} if empty
     * @see #poll()
     */
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return elementAt(0);
    }

    /**
     * Returns the number of elements currently in the queue.
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns whether the queue contains no elements.
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // -------- Internal Logic --------

    /**
     * Restores the heap invariant by sifting an element up from the given index.
     *
     * @param index index of the element to sift up
     */
    private void siftUp(int index) {
        E target = elementAt(index);

        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = elementAt(parentIndex);

            if (compare(target, parent) >= 0) {
                break;
            }

            elements[index] = parent;
            index = parentIndex;
        }

        elements[index] = target;
    }

    /**
     * Restores the heap invariant by sifting an element down from the given index.
     *
     * @param index index of the element to sift down
     */
    private void siftDown(int index) {
        E target = elementAt(index);

        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left >= size) {
                break;
            }

            int smallest = left;

            if (right < size &&
                    compare(elementAt(right), elementAt(left)) < 0) {
                smallest = right;
            }

            if (compare(elementAt(smallest), target) >= 0) {
                break;
            }

            elements[index] = elements[smallest];
            index = smallest;
        }

        elements[index] = target;
    }

    /**
     * Compares two elements using the configured comparator or their natural ordering.
     *
     * @param e1 first element
     * @param e2 second element
     * @return negative if {@code e1 < e2}, zero if equal, positive if {@code e1 > e2}
     */
    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<? super E>) e1).compareTo(e2);
    }

    /**
     * Ensures there is room to insert at least one more element, resizing the backing array if necessary.
     */
    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCapacity = elements.length * 2;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /**
     * Returns the element at the given index in the backing array.
     *
     * @param index index in the backing array
     * @return element at the specified index
     */
    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) elements[index];
    }
}
