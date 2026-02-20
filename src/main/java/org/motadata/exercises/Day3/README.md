# Playlist Service (Dynamic Array Focused)

## Overview

This project implements a **Music Playlist Service** using a custom dynamic array implementation. The service focuses on efficient song management, random-access playback (shuffle), and merging multiple playlists.

The system supports:
- **Dynamic Growth**: Automatically resizes as more songs are added.
- **True Shuffle**: Implements the Fisher-Yates shuffle algorithm for unbiased randomization.
- **Batch Merging**: Merges two playlists with pre-allocation optimization to minimize resizing overhead.

------------------------------------------------------------------------

# Architecture Design

The service manages an ordered collection of songs and maintains internal state for playback and randomization.

    PlaylistServiceArrayDynamic
     ├── songs (ArrayDynamic<String>)
     │     └── buffer (Object[]) - Resizable backing array
     ├── shuffleOrder (int[]) - Mapping for randomized playback
     └── currentIndex - Tracks current position in playback

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Playlist | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **Dynamic Array (Used)** | Standard Playlists | **O(1) Random Access** (crucial for shuffle), best cache locality, low memory overhead. | O(N) for insertion/deletion in the middle. |
| **LinkedList** | Queue-based Playlists | O(1) insertion at head/tail, no resizing overhead. | **O(N) Access**; to play the 50th song in shuffle, you must traverse 50 nodes. |
| **HashSet** | Unique Playlists | O(1) check for duplicates. | No inherent order; cannot support "play next" or specific indexing. |
| **LinkedHashMap** | Unique & Ordered | O(1) lookup + preserves insertion order. | Higher memory overhead per entry than a primitive array. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ ArrayDynamic (Custom Implementation)

``` java
ArrayDynamic<String> songs
```

**Why Dynamic Array?**
-   **O(1) Random Access**: Playlists frequently require jumping to a specific song (e.g., "Play Song #25"). In shuffle mode, we generate an array of indices; accessing these in an array is nearly instantaneous.
-   **Fisher-Yates Efficiency**: The shuffle algorithm requires swapping elements at random indices. This is O(1) in an array but would be O(N) in a LinkedList, making a shuffle operation O(N²) on a list.
-   **Predictable Growth**: Using a load factor (0.75) and doubling strategy ensures **O(1) Amortized** insertion time.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structure Involved | Time Complexity |
| :--- | :--- | :--- |
| `addSong()` | ArrayDynamic | **O(1)** (Amortized) |
| `nextSong()` | ArrayDynamic + Shuffle Mapping | **O(1)** |
| `enableShuffle()`| Fisher-Yates Logic | **O(N)** |
| `merge()` | ArrayList + `ensureCapacity` | **O(M)** (M = songs in other list) |

------------------------------------------------------------------------

# Design Tradeoffs

### Why use `ensureCapacity` during merge?
When merging two playlists, adding songs one-by-one might trigger multiple resize operations (copying the array multiple times). By calculating the `newSize` upfront and calling `songs.ensureCapacity(newSize)`, we resize the buffer **exactly once**, significantly improving performance for large batch operations.

### Array Shifting vs. Shuffle Mapping
Instead of physically shuffling the `songs` array (which would lose the original order), the service maintains a `shuffleOrder` array. This allows us to toggle shuffle mode ON and OFF instantly without destructive changes to the master list.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Amortized Analysis**: Understanding that while an individual `add` might be O(N) due to resize, the average cost over time is O(1).
-   **Load Factor Optimization**: Using a 75% threshold to balance memory waste against the frequency of expensive resize operations.
-   **Unbiased Randomization**: Using the Fisher-Yates algorithm to ensure every possible permutation of the playlist is equally likely.

------------------------------------------------------------------------

# Conclusion

The `PlaylistServiceArrayDynamic` demonstrates why **Arrays** are the foundation of performance-critical collections. By combining a resizable buffer with intelligent pre-allocation and indexing math, it provides a robust system capable of handling thousands of tracks with minimal latency.
