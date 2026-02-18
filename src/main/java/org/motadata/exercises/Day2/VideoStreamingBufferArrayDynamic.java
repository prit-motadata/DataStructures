package org.motadata.exercises.Day2;


import org.motadata.datastructures.array.ArrayDynamic;

public class VideoStreamingBufferArrayDynamic {

    private final ArrayDynamic<String> buffer;

    public VideoStreamingBufferArrayDynamic() {
        this.buffer = new ArrayDynamic<>();
    }

    public void addPacket(String packet) {
        buffer.add(packet);
    }

    public String getPacket(int index) {
        return buffer.get(index);
    }

    public int getBufferedPacketCount() {
        return buffer.size();
    }

    public int getBufferCapacity() {
        return buffer.capacity();
    }
}
