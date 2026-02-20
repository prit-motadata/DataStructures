package org.motadata.exercises.Day2;

import org.motadata.datastructures.queue.SlidingWindowCircularQueue;

/**
 * A video streaming buffer implementation using a sliding window circular
 * queue.
 *
 * <p>
 * This implementation handles packets with sequence numbers and maintains a
 * sliding window
 * for ordered retrieval.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class VideoStreamingBufferCircularQueue {

    private final SlidingWindowCircularQueue<String> windowBuffer;

    /**
     * Creates a video streaming buffer with the specified initial sequence and
     * window size.
     *
     * @param initialSequence the first expected sequence number
     * @param windowSize      the size of the sliding window
     */
    public VideoStreamingBufferCircularQueue(int initialSequence, int windowSize) {
        this.windowBuffer = new SlidingWindowCircularQueue<>(initialSequence, windowSize);
    }

    /**
     * Receives a packet and adds it to the buffer if it falls within the current
     * window.
     *
     * @param sequenceNumber the sequence number of the packet
     * @param data           the packet data (must be non-null)
     * @return {@code true} if the packet was added, {@code false} if it falls
     *         outside the window
     * @throws IllegalArgumentException if data is {@code null}
     */
    public boolean receivePacket(int sequenceNumber, String data) {
        if (data == null) {
            throw new IllegalArgumentException("Packet data cannot be null");
        }
        return windowBuffer.add(sequenceNumber, data);
    }

    /**
     * Retrieves and removes the next playable packet in order from the buffer.
     *
     * @return the next packet data, or {@code null} if it has not been received yet
     */
    public String playNext() {
        return windowBuffer.poll();
    }

    /**
     * Returns the number of packets currently stored in the buffer.
     *
     * @return count of buffered packets
     */
    public int bufferedPackets() {
        return windowBuffer.size();
    }

    /**
     * Returns the maximum capacity of the sliding window.
     *
     * @return window capacity
     */
    public int windowCapacity() {
        return windowBuffer.capacity();
    }

    /**
     * Returns the sequence number of the next packet expected to be played.
     *
     * @return next expected sequence number
     */
    public int nextExpectedSequence() {
        return windowBuffer.getBaseSequence();
    }
}
