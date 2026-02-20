# Seat Booking Service (Data Structure Focused)

## Overview

This project implements a **thread-safe seat booking system** using core Java data structures and custom fixed-size arrays. The primary focus is on balancing memory efficiency with high-concurrency performance using fine-grained locking.

The system supports:

-   Booking the next available seat.
-   Booking a specific seat by number.
-   Cancelling an existing booking.
-   Checking seat availability.
-   Listing all available seats.

------------------------------------------------------------------------

# Architecture Design

The service uses a direct mapping between seat numbers and array indices.

    SeatBookingService
     ├── seats (ArrayFixedSize<Integer>)
     └── seatLocks (ConcurrentHashMap<Integer, Object>)

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ Seats Storage

``` java
ArrayFixedSize<Integer> seats
```

**Why Fixed-Size Array?**

-   **Memory Efficiency:** Since the number of seats is constant, an array avoids the overhead of dynamic resizing and object wrappers found in `ArrayList` or `HashMap`.
-   **O(1) Access:** Direct index-based lookup (`index = seatNumber - 1`) for both reading status and updating state.
-   **Sequential Access:** Efficient for the `getAvailableSeats()` operation which requires linear scanning.

------------------------------------------------------------------------

## 2️⃣ Concurrency Management

``` java
ConcurrentHashMap<Integer, Object> seatLocks
```

**Why ConcurrentHashMap for Locks?**

-   **Fine-Grained Locking:** Instead of locking the entire `SeatBookingService` (which would prevent simultaneous bookings), we lock only the specific seat being modified.
-   **Thread Safety:** `computeIfAbsent` ensures that locks are created only once and shared consistently across threads.
-   **Scalability:** Allows high throughput by permitting multiple threads to operate on different seats concurrently.

------------------------------------------------------------------------

## 3️⃣ Available Seats Retrieval

``` java
List<Integer> result = new ArrayList<>()
```

**Why ArrayList?**

-   **Dynamic Growth:** The number of available seats is unknown until the scan is complete.
-   **O(1) Append:** Efficiently collects seat numbers during the linear scan.
-   **Order Preservation:** Maintains seat numbers in ascending order naturally during the scan.

------------------------------------------------------------------------

# Time Complexity Table

| Operation           | Data Structures Involved             | Time Complexity            |
|---------------------|--------------------------------------|----------------------------|
| `bookSeat()` (first)| ArrayFixedSize + ConcurrentHashMap   | O(N)                       |
| `bookSeat(number)`  | ArrayFixedSize + ConcurrentHashMap   | O(1)                       |
| `cancelSeat()`      | ArrayFixedSize + ConcurrentHashMap   | O(1)                       |
| `isSeatAvailable()` | ArrayFixedSize                       | O(1)                       |
| `getAvailableSeats()`| ArrayFixedSize + ArrayList           | O(N)                       |

------------------------------------------------------------------------

# Design Tradeoffs

### Why not synchronize the whole class?
Synchronizing the entire `bookSeat` method would be simple but would create a bottleneck where only one user could book *any* seat at a time. Using per-seat locks allows 100 users to book 100 different seats simultaneously.

### Why use `ArrayFixedSize` instead of `boolean[]`?
While a `boolean[]` would be even more memory-efficient, using the generic `ArrayFixedSize` abstraction allows for easier expansion (e.g., storing different seat types or user IDs instead of just a status flag) while maintaining the fixed-size constraint.

### Why scanning O(N) in `bookSeat()`?
We could use a `PriorityQueue` or `Queue` to store available seats for O(1) retrieval. However, maintaining that queue during `cancelSeat()` would require either O(N) removal or additional tracking structures, increasing the memory footprint and logic complexity. For most theater-sized applications, O(N) scanning is extremely fast.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Lock Striping / Per-Entity Locking:** Minimizing contention by narrowing the scope of synchronization.
-   **Index Mapping:** Mapping business logic IDs (Seat 1) directly to memory locations (Index 0).
-   **Lazy Initialization:** Creating lock objects only when a seat is actually interacted with, saving memory for unaccessed seats.

------------------------------------------------------------------------

# Conclusion

This project demonstrates how to implement a high-performance resource allocation system by:
- Choosing **ArrayFixedSize** for predictable memory and O(1) access.
- Implementing **Fine-grained locking** to maximize concurrency.
- Reasoning about **Time vs. Complexity** when choosing between linear scans and auxiliary tracking structures.
