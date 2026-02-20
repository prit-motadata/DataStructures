package org.motadata.datastructures.array;

import java.util.Arrays;

/**
 * Dynamically resizing array implementation that grows based on a configurable load factor.
 *
 * @param <T> type of elements stored in the array
 * @author prit.thakkar@motadata.com
 */
public class ArrayDynamic<T> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private T[] buffer;
    private int size;
    private final double loadFactor;

    /**
     * Creates a new dynamic array with default capacity and load factor.
     */
    @SuppressWarnings("unchecked")
    public ArrayDynamic() {
        this.buffer = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Appends an element to the end of this array, resizing the backing buffer if needed.
     *
     * @param element element to be added
     * @see #ensureCapacity(int)
     */
    public void add(T element) {
        if (shouldResize()) {
            resize();
        }
        buffer[size++] = element;
    }

    /**
     * Replaces the element at the specified index.
     *
     * @param index position of the element to replace
     * @param value new value to store at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @see #get(int)
     */
    public void set(int index, T value) {
        checkIndex(index);
        buffer[index] = value;
    }

    /**
     * Determines whether the internal buffer should be resized based on the current size
     * and configured load factor.
     *
     * @return {@code true} if the buffer should be grown, {@code false} otherwise
     */
    private boolean shouldResize() {
        return size >= buffer.length * loadFactor;
    }

    /**
     * Doubles the capacity of the internal buffer while preserving existing elements.
     */
    private void resize() {
        buffer = Arrays.copyOf(buffer, buffer.length * 2);
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index position of the element to return
     * @return element stored at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @see #set(int, Object)
     */
    public T get(int index) {
        checkIndex(index);
        return buffer[index];
    }

    /**
     * Returns the number of elements currently stored in the array.
     *
     * @return current logical size
     */
    public int size() {
        return size;
    }

    /**
     * Returns the current capacity of the backing buffer.
     *
     * @return maximum number of elements that can be stored without resizing
     */
    public int capacity() {
        return buffer.length;
    }

    /**
     * Ensures that the backing buffer can hold at least the given number of elements.
     * If necessary, the capacity is increased by repeatedly doubling the current size.
     *
     * @param minCapacity minimum required capacity
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > buffer.length) {
            int newCapacity = buffer.length;
            while (newCapacity < minCapacity) {
                newCapacity *= 2;
            }
            buffer = Arrays.copyOf(buffer, newCapacity);
        }
    }

    /**
     * Validates that the provided index is within bounds for the current logical size.
     *
     * @param index index to validate
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }
}
