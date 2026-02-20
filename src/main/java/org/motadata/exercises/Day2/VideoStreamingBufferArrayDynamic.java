package org.motadata.exercises.Day2;

import org.motadata.datastructures.array.ArrayDynamic;

/**
 * A video streaming buffer implementation using a dynamic array.
 *
 * <p>
 * This implementation stores packets in the order they are received.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class VideoStreamingBufferArrayDynamic {

    private final ArrayDynamic<String> buffer;

    /**
     * Creates an empty video streaming buffer.
     */
    public VideoStreamingBufferArrayDynamic() {
        this.buffer = new ArrayDynamic<>();
    }

    /**
     * Adds a packet to the buffer.
     *
     * @param packet the packet data to be added
     */
    public void addPacket(String packet) {
        buffer.add(packet);
    }

    /**
     * Retrieves a packet from the buffer at the specified index.
     *
     * @param index the index of the packet to retrieve
     * @return the packet data at the given index
     */
    public String getPacket(int index) {
        return buffer.get(index);
    }

    /**
     * Returns the number of packets currently stored in the buffer.
     *
     * @return current buffered packet count
     */
    public int getBufferedPacketCount() {
        return buffer.size();
    }

    /**
     * Returns the current capacity of the internal buffer.
     *
     * @return internal buffer capacity
     */
    public int getBufferCapacity() {
        return buffer.capacity();
    }
}
