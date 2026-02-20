# Video Streaming Buffer (Sliding Window Circular Queue)

## Overview

This project implements a **Video Streaming Buffer** designed to handle out-of-order network packets. It uses a **Sliding Window Circular Queue** to ensure that video frames are played in the correct sequence, even if they arrive at the receiver out of order or with gaps.

The system supports:
- **Ordered Playback**: Only returns the next sequential packet.
- **Out-of-Order Handling**: Accepts packets within a specific "window" of the current playback point.
- **Fixed Memory Footprint**: Uses a circular buffer to reuse memory efficiently.

------------------------------------------------------------------------

# Architecture Design

The service acts as a wrapper around a specialized circular queue that tracks sequence numbers.

    VideoStreamingBufferCircularQueue
     └── windowBuffer (SlidingWindowCircularQueue<String>)
           ├── buffer (Object[]) - Stores packet data
           ├── received (boolean[]) - Tracks which sequences have arrived
           └── headIndex - Points to the next playable sequence

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Streaming | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **Circular Queue (Used)** | Buffer with fixed window size | O(1) random access by sequence, O(1) sliding, memory efficient. | Fixed capacity; cannot handle jumps larger than window size. |
| **TreeMap** | Storing packets by sequence | Automatically sorted, handles any sequence range. | O(log N) insertion/removal, higher memory overhead per entry. |
| **PriorityQueue (Min-Heap)** | Reordering packets | O(log N) insertion, O(1) peek at smallest. | Cannot do O(1) "is packet X already here?" check; O(N) to remove specific sequence. |
| **ArrayList** | Global history | Fast append. | O(N) to insert out-of-order packets at the correct index (shifting). |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ Circular Buffer (Fixed Array)

``` java
Object[] buffer
boolean[] received
```

**Why Circular Buffer?**
- **O(1) Direct Access**: Using `(headIndex + offset) % windowSize`, we can place a packet arriving out-of-order (e.g., Seq 10 arriving before Seq 9) directly into its correct "slot" in constant time.
- **Zero Shifting**: Unlike an `ArrayList`, sliding the window forward only requires incrementing the `headIndex`. No elements are moved in memory.
- **Minimal GC Pressure**: The arrays are allocated once and reused. This is critical for high-throughput video streaming where garbage collection pauses can cause stuttering (jitter).

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structure Involved | Time Complexity |
| :--- | :--- | :--- |
| `receivePacket()` | SlidingWindow (Array Access) | **O(1)** |
| `playNext()` | SlidingWindow (Index Increment) | **O(1)** |
| `bufferedPackets()` | Counter variable | **O(1)** |
| `windowCapacity()` | Constant field | **O(1)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Why use `boolean[] received` separately?
The `buffer` array stores the generic payload `T`. However, a slot might be `null` either because it hasn't been received yet OR because the payload itself is `null`. The `received` array provides an unambiguous O(1) check for presence.

### Why not use a `PriorityQueue`?
While a `PriorityQueue` (Min-Heap) would always give us the smallest sequence number, it doesn't solve the "missing packet" problem efficiently. To know if we can play the *next* sequential packet, we'd have to peek at the heap and compare it to our `expectedSequence`. If the packet is missing, we wait. However, `PriorityQueue` does not allow us to place packet #10 in its spot if packets #7, #8, and #9 are still missing without a search. The Circular Queue provides direct "slot" mapping via sequence math.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Sequence Math**: Using modulo arithmetic to map an infinite sequence space into a finite memory buffer.
-   **Sliding Window Protocol**: Only accepting data that is "relevant" (within the window) to prevent memory exhaustion from rogue or extremely late packets.
-   **Separation of Concerns**: `VideoStreamingBuffer` handles application logic, while `SlidingWindowCircularQueue` handles the low-level data structure mechanics.

------------------------------------------------------------------------

# Conclusion

The `VideoStreamingBufferCircularQueue` is an optimized solution for real-time data reordering. By using a **Circular Buffer**, it achieves the performance of an array with the logical behavior of a queue, making it ideal for the low-latency requirements of video playback.
