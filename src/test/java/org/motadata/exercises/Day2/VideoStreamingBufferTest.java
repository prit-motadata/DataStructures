package org.motadata.exercises.Day2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoStreamingBufferTest {

    @Test
    void shouldUseDefaultCapacity() {
        VideoStreamingBuffer buffer = new VideoStreamingBuffer();

        assertEquals(0, buffer.getBufferedPacketCount());
        assertEquals(16, buffer.getBufferCapacity());
    }

    @Test
    void shouldAddPacketsWithoutResizeBeforeLoadFactor() {
        VideoStreamingBuffer buffer = new VideoStreamingBuffer();

        // Add up to load factor threshold (12)
        for (int i = 0; i < 12; i++) {
            buffer.addPacket("Packet-" + i);
        }

        assertEquals(12, buffer.getBufferedPacketCount());
        assertEquals(16, buffer.getBufferCapacity()); // no resize yet
    }

    @Test
    void shouldResizeWhenLoadFactorIsExceeded() {
        VideoStreamingBuffer buffer = new VideoStreamingBuffer();

        // 13th insert triggers resize
        for (int i = 0; i < 13; i++) {
            buffer.addPacket("Packet-" + i);
        }

        assertEquals(13, buffer.getBufferedPacketCount());
        assertEquals(32, buffer.getBufferCapacity());
    }

    @Test
    void shouldPreservePacketOrderAfterResize() {
        VideoStreamingBuffer buffer = new VideoStreamingBuffer();

        buffer.addPacket("Frame1");
        buffer.addPacket("Frame2");
        buffer.addPacket("Frame3");

        assertEquals("Frame1", buffer.getPacket(0));
        assertEquals("Frame2", buffer.getPacket(1));
        assertEquals("Frame3", buffer.getPacket(2));
    }

    @Test
    void shouldThrowExceptionForInvalidIndexAccess() {
        VideoStreamingBuffer buffer = new VideoStreamingBuffer();

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> buffer.getPacket(0)
        );
    }
}
