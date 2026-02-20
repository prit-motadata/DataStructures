package org.motadata.datastructures.array;

import java.util.Arrays;

/**
 * Fixed-size array wrapper that provides bounds-checked access and bulk operations.
 *
 * @param <T> type of elements stored in the array
 * @author prit.thakkar@motadata.com
 */
public class ArrayFixedSize<T> {
    private final T[] array;

    /**
     * Creates a new fixed-size array with the given length.
     *
     * @param size number of elements in the array
     */
    @SuppressWarnings("unchecked")
    public ArrayFixedSize(int size) {
        array = (T[]) new Object[size];
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
        return array[index];
    }

    /**
     * Sets the element at the specified index to the given value.
     *
     * @param index index to modify
     * @param value value to store at the index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @see #get(int)
     */
    public void set(int index, T value) {
        checkIndex(index);
        array[index] = value;
    }

    /**
     * Returns the total length of this fixed-size array.
     *
     * @return number of elements this array can hold
     */
    public int size() {
        return array.length;
    }

    /**
     * Fills the entire array with the given value.
     *
     * @param value value to assign to each element
     */
    public void fill(T value) {
        Arrays.fill(array, value);
    }

    /**
     * Ensures that the provided index is within the valid range \([0, size())\).
     *
     * @param index index to validate
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + array.length
            );
        }
    }
}
