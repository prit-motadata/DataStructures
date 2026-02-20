package org.motadata.datastructures.queue;

/**
 * Sliding window queue for ordered packet delivery backed by a circular buffer.
 *
 * <p>Packets are identified by sequence numbers; only packets within the current window are accepted,
 * and {@link #poll()} returns the next in-order packet when available while advancing the window.</p>
 *
 * @param <T> payload type stored in the window
 * @author prit.thakkar@motadata.com
 */
public class SlidingWindowCircularQueue<T> {
    private final Object[] buffer;
    private final boolean[] received;
    private final int windowSize;

    private int baseSequence;
    private int headIndex;
    private int currentSize;

    /**
     * Creates a new sliding window starting at the given initial sequence with the specified window size.
     *
     * @param initialSequence starting sequence number for the window
     * @param windowSize      number of sequence slots tracked at any time
     * @throws IllegalArgumentException if {@code windowSize} is not positive
     */
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

    /**
     * Adds a packet with the given sequence number to the window if it falls within the current range.
     *
     * @param sequenceNumber sequence number associated with the packet
     * @param data           packet payload
     * @return {@code true} if the packet was accepted, {@code false} if it was outside the window
     */
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

    /**
     * Retrieves and removes the next in-order packet from the head of the window, if present.
     *
     * <p>If the head sequence has not yet been received, this method returns {@code null}
     * and the window does not advance.</p>
     *
     * @return next in-order packet payload, or {@code null} if not yet available
     */
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

    /**
     * Returns the number of packets currently buffered in the window.
     *
     * @return buffered packet count
     */
    public int size() {
        return currentSize;
    }

    /**
     * Returns the fixed capacity of this sliding window.
     *
     * @return maximum number of sequence slots held at any time
     */
    public int capacity() {
        return windowSize;
    }

    /**
     * Returns the base (next expected) sequence number for this window.
     *
     * @return current base sequence number
     */
    public int getBaseSequence() {
        return baseSequence;
    }
}
