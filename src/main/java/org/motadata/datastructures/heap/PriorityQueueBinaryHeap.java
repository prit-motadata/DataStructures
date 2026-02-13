package org.motadata.datastructures.heap;

import java.util.Arrays;
import java.util.Comparator;

public class PriorityQueueBinaryHeap<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    // -------- Constructors --------

    public PriorityQueueBinaryHeap() {
        this(DEFAULT_CAPACITY, null);
    }

    public PriorityQueueBinaryHeap(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public PriorityQueueBinaryHeap(int capacity, Comparator<? super E> comparator) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.elements = new Object[capacity];
        this.comparator = comparator;
    }

    // -------- Public APIs --------

    public void offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity();
        elements[size] = element;
        siftUp(size);
        size++;
    }

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

    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return elementAt(0);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // -------- Internal Logic --------

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

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<? super E>) e1).compareTo(e2);
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCapacity = elements.length * 2;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) elements[index];
    }
}
