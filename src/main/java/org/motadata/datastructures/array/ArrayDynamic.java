package org.motadata.datastructures.array;

import java.util.Arrays;

public class ArrayDynamic<T> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private T[] buffer;
    private int size;
    private final double loadFactor;

    @SuppressWarnings("unchecked")
    public ArrayDynamic() {
        this.buffer = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    // Add element
    public void add(T element) {
        if (shouldResize()) {
            resize();
        }
        buffer[size++] = element;
    }

    public void set(int index, T value) {
        checkIndex(index);
        buffer[index] = value;
    }

    private boolean shouldResize() {
        return size >= buffer.length * loadFactor;
    }

    // Double capacity
    private void resize() {
        buffer = Arrays.copyOf(buffer, buffer.length * 2);
    }

    public T get(int index) {
        checkIndex(index);
        return buffer[index];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buffer.length;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }
}
