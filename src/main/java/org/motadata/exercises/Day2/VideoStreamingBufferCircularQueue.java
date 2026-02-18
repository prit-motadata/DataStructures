package org.motadata.exercises.Day2;

import org.motadata.datastructures.queue.SlidingWindowCircularQueue;

public class VideoStreamingBufferCircularQueue {

    private final SlidingWindowCircularQueue<String> windowBuffer;

    public VideoStreamingBufferCircularQueue(int initialSequence, int windowSize) {
        this.windowBuffer =
                new SlidingWindowCircularQueue<>(initialSequence, windowSize);
    }

    // Add a packet to buffer.
    public boolean receivePacket(int sequenceNumber, String data) {
        if (data == null) {
            throw new IllegalArgumentException("Packet data cannot be null");
        }
        return windowBuffer.add(sequenceNumber, data);
    }

    // Get next playable packet in order.
    public String playNext() {
        return windowBuffer.poll();
    }

    // Number of packets currently buffered.
    public int bufferedPackets() {
        return windowBuffer.size();
    }

    // Window capacity.
    public int windowCapacity() {
        return windowBuffer.capacity();
    }

    // Next expected sequence number.
    public int nextExpectedSequence() {
        return windowBuffer.getBaseSequence();
    }
}
