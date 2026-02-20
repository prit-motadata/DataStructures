# Browser Tabs Manager (Linked Hash Map Focused)

## Overview

This project implements a **Browser Tab Management System** using a custom **Linked Hash Map**. It optimizes for the two most common browser behaviors: finding a specific tab instantly (O(1)) and maintaining the specific visual or logical order of tabs (Insertion or Access Order).

The system supports:
- **Instant Search**: Find if a tab is open in constant time.
- **Configurable Ordering**: 
    - *Insertion Order*: Traditional tab behavior (newest at the end).
    - *Access Order (MRU)*: Tracks which tabs you use most recently, perfect for "Most Recently Used" tab switching (Ctrl-Tab).
- **Efficient Closing**: Removing a tab from the middle of the set without expensive array-shifting.

------------------------------------------------------------------------

# Architecture Design

The service leverages a hybrid data structure that combines a Hash Table with a Doubly Linked List.

    BrowserTabsManagerCHM
     └── tabs (CustomLinkedHashMap<String, Boolean>)
           ├── table (Node<K,V>[]) - Hash table for O(1) lookup
           └── head/tail (Node<K,V>) - Doubly linked pointers for O(1) ordering

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Tabs | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **LinkedHashMap (Used)** | Modern Tab Management | **O(1) Search + O(1) Ordering**. Best of both worlds. | Slightly higher memory (3 pointers per node). |
| **HashMap** | Basic Storage | O(1) Search. | **Loss of order**. Cannot display tabs in the order they were opened. |
| **ArrayList** | Simple Tab List | Preserves order. | **O(N) Search/Close**. Closing a tab at index 0 requires shifting all subsequent tabs. |
| **TreeMap** | Alphabetical Tabs | Always sorted by name. | O(log N) operations; slower than hashing. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ CustomLinkedHashMap (The Hybrid)

``` java
private final CustomLinkedHashMap<String, Boolean> tabs;
```

**Why this specific implementation?**
-   **High Performance Hashing**: Uses the `hash()` function to distribute tabs across buckets, ensuring that finding a tab name like "google.com" doesn't slow down even if 100 tabs are open.
-   **Doubly Linked List (`before`/`after`)**: Every "Tab" node in the map knows its predecessor and successor. When a tab is accessed in `accessOrder` mode, we can "extract" it and "append" it to the end in **O(1)** time.
-   **Collision Handling**: Uses **Chaining** (linked nodes in each bucket) to handle cases where two different tab names hash to the same index.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structure Involved | Time Complexity |
| :--- | :--- | :--- |
| `openTab()` | Hash Table + LinkLast | **O(1)** |
| `search()` | Hash Table | **O(1)** |
| `closeTab()` | Hash Table + Unlink | **O(1)** |
| `displayTabs()` | Doubly Linked List Traversal | **O(N)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Why not just use two separate collections (Map + List)?
Maintaining a separate `HashMap<String, Node>` and `LinkedList<String>` is possible, but it doubles the management logic. Synchronizing removals between two collections is error-prone. By integrating the pointers directly into the `Map.Node`, we achieve atomic updates and better memory locality.

### Access Order vs. Insertion Order
- **Insertion Order** is ideal for users who want their tabs to stay exactly where they put them.
- **Access Order** is powerful for internal browser logic like "Least Recently Used (LRU) Cache". If a browser runs out of memory, it can use this structure to find and kill the tab at the `head` of the list (the one used longest ago) in O(1) time.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Memory-Speed Tradeoff**: We use extra memory (3 pointers per node) to gain significant speed in ordering operations.
-   **Iterator Pattern**: The `CustomLinkedHashMap` implements `Iterable`, allowing the `BrowserTabsManager` to loop through tabs using the linked list instead of scanning the sparse hash table.
-   **Load Factor Thresholding**: Automatically doubles the internal capacity (`resize()`) once the map is 75% full to prevent hash collisions from degrading performance.

------------------------------------------------------------------------

# Conclusion

The `BrowserTabsManagerCHM` showcases how sophisticated data structures are often **composites**. By overlaying a linked list on top of a hash table, we solve the "indexing vs. ordering" dilemma, creating a system that is both lightning-fast for lookups and strictly disciplined with sequence.
