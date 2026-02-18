package org.motadata.exercises.Day2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoStreamingBufferCircularQueueTest {

    @Test
    void testConstructorAndBasicProperties() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        assertEquals(5, buffer.windowCapacity());
        assertEquals(0, buffer.bufferedPackets());
        assertEquals(100, buffer.nextExpectedSequence());
    }

    @Test
    void testReceivePacketValid() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        boolean accepted = buffer.receivePacket(100, "FrameA");

        assertTrue(accepted);
        assertEquals(1, buffer.bufferedPackets());
    }

    @Test
    void testReceivePacketNullDataThrowsException() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        assertThrows(IllegalArgumentException.class,
                () -> buffer.receivePacket(100, null));
    }

    @Test
    void testReceivePacketOutsideWindowReturnsFalse() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        boolean accepted = buffer.receivePacket(200, "OutOfWindow");

        assertFalse(accepted);
        assertEquals(0, buffer.bufferedPackets());
    }

    @Test
    void testPlayNextInOrder() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        buffer.receivePacket(100, "A");
        buffer.receivePacket(102, "C");
        buffer.receivePacket(101, "B");

        assertEquals("A", buffer.playNext());
        assertEquals(101, buffer.nextExpectedSequence());

        assertEquals("B", buffer.playNext());
        assertEquals("C", buffer.playNext());

        assertNull(buffer.playNext());
        assertEquals(0, buffer.bufferedPackets());
    }

    @Test
    void testPlayNextWhenMissingPacket() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(100, 5);

        buffer.receivePacket(101, "B"); // Missing 100

        assertNull(buffer.playNext());
        assertEquals(1, buffer.bufferedPackets());
        assertEquals(100, buffer.nextExpectedSequence());
    }

    @Test
    void testWindowWrapAroundBehavior() {
        VideoStreamingBufferCircularQueue buffer =
                new VideoStreamingBufferCircularQueue(1, 3);

        buffer.receivePacket(1, "A");
        buffer.receivePacket(2, "B");
        buffer.receivePacket(3, "C");

        assertEquals("A", buffer.playNext());
        assertEquals("B", buffer.playNext());
        assertEquals("C", buffer.playNext());

        assertEquals(4, buffer.nextExpectedSequence());
        assertEquals(0, buffer.bufferedPackets());
    }
}
