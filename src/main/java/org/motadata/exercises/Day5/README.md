# User Login Service (Map & Radix Tree Focused)

## Overview

This project implements a **User Registration and Authentication Service** that optimizes for two distinct access patterns: instant credential verification and efficient username autocomplete (prefix searching).

The system supports:
- **Instant Authentication**: Validate user credentials in constant time.
- **Prefix-Based Discovery**: Quickly find users whose names start with a specific string (e.g., for "mention" systems or search bars).
- **Space-Efficient Indexing**: Uses a Radix Tree (Compact Trie) to reduce memory consumption for stored usernames.

------------------------------------------------------------------------

# Architecture Design

The service uses a dual-structure approach to provide O(1) lookups and efficient prefix matching.

    UserLoginService
     ├── users (Map<String, String>) - Stores Username → Password
     └── userIndex (RadixTree) - Compressed index of usernames

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Login System | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **Radix Tree (Used)** | Username Discovery | **O(L) Prefix Search** (L=prefix length), compressed nodes save memory. | Slightly more complex implementation than a basic Trie. |
| **Standard Trie** | Basic Autocomplete | Efficient prefix search. | **High Memory Waste**; creates a node for every single character. |
| **HashMap** | Authentication Storage | **O(1) Exact Lookup**. | **Cannot do Prefix Search**; would require scanning all entries (O(N)). |
| **HashSet** | Uniqueness Check | O(1) membership check. | No password storage and no prefix search support. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ Map (Hashing)
``` java
private final Map<String, String> users;
```
**Why Map?**
- **O(1) Authentication**: When a user logs in, we need to find their password instantly. A hash-based map allows us to bypass searching through thousands of users and jump directly to the credentials.
- **Identity Uniqueness**: Naturally handles username collisions (registration fails if the key already exists).

## 2️⃣ Radix Tree (Compact Trie)
``` java
private final RadixTree userIndex;
```
**Why Radix Tree?**
- **Edge Compression**: Unlike a normal Trie where "apple" would take 5 nodes, a Radix Tree merges shared paths. If "apple" is the only word, it's 1 node. This drastically reduces the memory footprint for large user bases.
- **Predictable Performance**: Search time depends strictly on the length of the prefix, not the number of users in the system.
- **Autocomplete Optimization**: Specifically designed to "Collect All" children of a prefix node, making it the industry standard for search-as-you-type features.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structure Involved | Time Complexity |
| :--- | :--- | :--- |
| `registerUser()` | HashMap + Radix Tree | **O(L)** (L = username length) |
| `authenticate()` | HashMap | **O(1)** (Average) |
| `deleteUser()` | HashMap + Radix Tree | **O(L)** |
| `searchUsersByPrefix()` | Radix Tree | **O(P + K)** (P=prefix, K=results) |

------------------------------------------------------------------------

# Design Tradeoffs

### Why use two data structures for one service?
No single data structure is optimal for both exact-match (auth) and prefix-match (discovery). 
- A **HashMap** is terrible at prefix searching (O(N)).
- A **Radix Tree** is slower than a HashMap for exact lookups because it requires walking down nodes character by character.
By using **both**, we trade a small amount of memory to get maximum speed for both user journeys.

### Why Radix Tree instead of Standard Trie?
A standard Trie is a "memory hog" because it creates many nodes with a single child. In a user database with millions of unique strings, a Radix Tree's ability to merge edges (e.g., merging "inter" and "net" into "internet") saves significant heap space and reduces pointer-chasing during traversal.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Redundancy for Speed**: Maintaining an auxiliary index (`userIndex`) to solve a performance bottleneck that the primary storage (`users` map) cannot handle efficiently.
-   **Radix Compression**: The "compacting" of nodes to optimize for both space and cache locality.
-   **Separation of Concerns**: Authentication logic is decoupled from search/indexing logic.

------------------------------------------------------------------------

# Conclusion

The `UserLoginService` demonstrates a production-grade approach to user management. By combining the **O(1) strength of Hashing** for security with the **O(L) strength of Radix Trees** for discovery, it builds a system that is both robust for authentication and fluid for user interaction.
