package org.motadata.datastructures.array;

import java.util.Arrays;

public class ArrayFixedSize<T> {
    private final T[] array;

    @SuppressWarnings("unchecked")
    public ArrayFixedSize(int size) {
        array = (T[]) new Object[size];
    }

    public T get(int index) {
        checkIndex(index);
        return array[index];
    }

    public void set(int index, T value) {
        checkIndex(index);
        array[index] = value;
    }

    public int size() {
        return array.length;
    }

    public void fill(T value) {
        Arrays.fill(array, value);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + array.length
            );
        }
    }
}
