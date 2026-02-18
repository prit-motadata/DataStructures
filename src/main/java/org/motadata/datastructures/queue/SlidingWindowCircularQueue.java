package org.motadata.datastructures.queue;

public class SlidingWindowCircularQueue<T> {
    private final Object[] buffer;
    private final boolean[] received;
    private final int windowSize;

    private int baseSequence;
    private int headIndex;
    private int currentSize;

    public SlidingWindowCircularQueue(int initialSequence, int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }

        this.windowSize = windowSize;
        this.baseSequence = initialSequence;
        this.headIndex = 0;
        this.buffer = new Object[windowSize];
        this.received = new boolean[windowSize];
        this.currentSize = 0;
    }

    // Add packet
    public boolean add(int sequenceNumber, T data) {

        if (sequenceNumber < baseSequence ||
                sequenceNumber >= baseSequence + windowSize) {
            return false; // outside window
        }

        int offset = sequenceNumber - baseSequence;
        int index = (headIndex + offset) % windowSize;

        if (!received[index]) {
            currentSize++;
        }

        buffer[index] = data;
        received[index] = true;

        return true;
    }

    // Poll next in-order packet
    @SuppressWarnings("unchecked")
    public T poll() {

        if (!received[headIndex]) {
            return null;
        }

        T result = (T) buffer[headIndex];

        buffer[headIndex] = null;
        received[headIndex] = false;
        currentSize--;

        // Slide window (O(1))
        headIndex = (headIndex + 1) % windowSize;
        baseSequence++;

        return result;
    }

    public int size() {
        return currentSize;
    }

    public int capacity() {
        return windowSize;
    }

    public int getBaseSequence() {
        return baseSequence;
    }
}
