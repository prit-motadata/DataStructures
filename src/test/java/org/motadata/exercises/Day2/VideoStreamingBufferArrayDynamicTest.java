package org.motadata.exercises.Day2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoStreamingBufferArrayDynamicTest {

    @Test
    void shouldUseDefaultCapacity() {
        VideoStreamingBufferArrayDynamic buffer = new VideoStreamingBufferArrayDynamic();

        assertEquals(0, buffer.getBufferedPacketCount());
        assertEquals(16, buffer.getBufferCapacity());
    }

    @Test
    void shouldAddPacketsWithoutResizeBeforeLoadFactor() {
        VideoStreamingBufferArrayDynamic buffer = new VideoStreamingBufferArrayDynamic();

        // Add up to load factor threshold (12)
        for (int i = 0; i < 12; i++) {
            buffer.addPacket("Packet-" + i);
        }

        assertEquals(12, buffer.getBufferedPacketCount());
        assertEquals(16, buffer.getBufferCapacity()); // no resize yet
    }

    @Test
    void shouldResizeWhenLoadFactorIsExceeded() {
        VideoStreamingBufferArrayDynamic buffer = new VideoStreamingBufferArrayDynamic();

        // 13th insert triggers resize
        for (int i = 0; i < 13; i++) {
            buffer.addPacket("Packet-" + i);
        }

        assertEquals(13, buffer.getBufferedPacketCount());
        assertEquals(32, buffer.getBufferCapacity());
    }

    @Test
    void shouldPreservePacketOrderAfterResize() {
        VideoStreamingBufferArrayDynamic buffer = new VideoStreamingBufferArrayDynamic();

        buffer.addPacket("Frame1");
        buffer.addPacket("Frame2");
        buffer.addPacket("Frame3");

        assertEquals("Frame1", buffer.getPacket(0));
        assertEquals("Frame2", buffer.getPacket(1));
        assertEquals("Frame3", buffer.getPacket(2));
    }

    @Test
    void shouldThrowExceptionForInvalidIndexAccess() {
        VideoStreamingBufferArrayDynamic buffer = new VideoStreamingBufferArrayDynamic();

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> buffer.getPacket(0)
        );
    }
}
