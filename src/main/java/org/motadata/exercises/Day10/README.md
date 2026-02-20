# URL Shortener Service (Concurrent & Base62 Focused)

## Overview

This project implements a **High-Concurrency URL Shortening Service**. It focuses on generating short, unique identifiers for long URLs using **Base62 Encoding** and ensuring thread-safety during generation and storage.

The system supports:
- **Atomic Code Generation**: Uses an `AtomicLong` counter to guarantee unique sequence numbers.
- **Base62 Encoding**: Compresses numeric IDs into short, URL-friendly alphanumeric strings.
- **Concurrent Storage**: Utilizes `ConcurrentHashMap` to handle high-volume read/write traffic without global locking.
- **Collision Safety**: Implements optimistic "put-if-absent" logic to ensure data integrity.

------------------------------------------------------------------------

# Architecture Design

The service decouples the generation of codes from the storage of mappings.

    UrlShortenerServiceConcurrent
     ├── urlStore (ConcurrentMap<String, String>) - Alphanumeric Code → Original URL
     └── generator (UrlGenerator)
           └── counter (AtomicLong) - Global unique sequence provider

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in URL Shortening | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **ConcurrentHashMap (Used)** | URL Mapping Storage | **O(1) Search/Insert**. Multi-thread safe. | Higher memory usage than simple map. |
| **AtomicLong (Used)** | ID Generation | **Lock-free uniqueness**. Scaling across threads. | Limited to a single machine/JVM. |
| **HashMap** | Single-threaded Storage | Lightweight. | **Not thread-safe**. Leads to data loss or crashes under load. |
| **ArrayList** | Linear Mapping | Simple. | **O(N) Retrieval**. Finding a URL by code requires scanning the whole list. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ ConcurrentHashMap (Storage)

``` java
private final ConcurrentMap<String, String> urlStore = new ConcurrentHashMap<>();
```

**Why this?**
-   **Lock Striping**: Allows many threads to shorten URLs and resolve redirections simultaneously by only locking small "segments" of the map.
-   **Atomic `putIfAbsent`**: Crucial for the `do-while` loop. It ensures that if two threads accidentally generate the same code (impossible with our `AtomicLong` but good practice), only one succeeding write wins without corrupting state.

## 2️⃣ AtomicLong (ID Source)

``` java
private final AtomicLong counter = new AtomicLong(1);
```

**Why this?**
-   **Zero Contention**: Unlike `synchronized`, `AtomicLong` uses hardware CAS (Compare-And-Swap) instructions. This makes generating the "next" ID extremely fast even when hundreds of threads call it per second.
-   **Sequential Uniqueness**: Guarantees that every call to `generate()` returns a number exactly 1 higher than the last, providing a solid foundation for unique short codes.

## 3️⃣ Base62 Encoding (Representation)

``` java
private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
```

**Why Base62?**
-   **Shortness**: A number like `1,000,000,000` is 10 digits in Base10, but only 5 characters in Base62.
-   **URL Friendly**: Uses only `0-9`, `a-z`, and `A-Z`, which require no special encoding in web browsers.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structures Involved | Time Complexity |
| :--- | :--- | :--- |
| `shortenUrl()` | AtomicLong + Base62 + Hash Map | **O(1)** (Technically O(L) where L=code length) |
| `getOriginalUrl()` | Hash Map Lookup | **O(1)** |
| `totalUrls()` | Concurrent Map Size | **O(1)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Why use a counter instead of random strings?
- **Random strings** (like UUIDs) are long and carry a risk of collisions. A **Counter** is guaranteed to be unique for $2^{64}$ entries.
- Using a counter makes the short links "predictable" (which might be a security concern), but for a high-performance system, it is the most efficient way to guarantee uniqueness without expensive "check if exists" database hits.

### StringBuilder vs. String
In `encodeBase62`, we use `StringBuilder` to build the code. This is much faster than `String +=` because it avoids creating multiple temporary objects in the heap, reducing garbage collection pressure during high-traffic bursts.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Optimistic Concurrency**: Using `putIfAbsent` instead of wrapping the entire method in a `lock`.
-   **Sequence-to-String Mapping**: Converting numeric business identities into compact, human-readable alphanumeric strings.
-   **Lock-Free State**: Leveraging atomic primitives to manage the most contention-heavy part of the system (the counter).

------------------------------------------------------------------------

# Conclusion

The `UrlShortenerServiceConcurrent` demonstrates the basic building blocks of systems like Bitly or TinyURL. By combining **Atomic Sequencing** for identity with **Hash Mapping** for storage, it creates a robust, scalable engine capable of handling millions of redirections with sub-millisecond latency.
