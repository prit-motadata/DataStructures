# Thread-Safe Shopping Cart (Concurrency Focused)

## Overview

This project implements a **High-Performance Concurrent Shopping Cart** using Java's `java.util.concurrent` package. It focuses on thread-safety without the bottleneck of global synchronization, allowing multiple threads to add items, apply discounts, and manage wishlists simultaneously.

The system supports:
- **Lock-Free Totals**: Atomic updates to the cart total using `AtomicReference`.
- **Concurrent Item Management**: Thread-safe cart operations using lock-striping.
- **Efficient Wishlists**: Unordered, thread-safe membership checks.
- **Atomic Compound Operations**: Correctly handling "check-then-act" logic using atomic compute methods.

------------------------------------------------------------------------

# Architecture Design

The service utilizes several specialized concurrent structures to ensure data consistency under high load.

    ShoppingCart
     ├── items (ConcurrentHashMap<String, CartItem>) - Segmented locking for O(1) access
     ├── discounts (ConcurrentHashMap<String, Discount>) - Static discount registry
     ├── wishlist (Set<String>) - Created via ConcurrentHashMap.newKeySet()
     └── total (AtomicReference<Double>) - Lock-free numeric accumulator

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in eCommerce | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **ConcurrentHashMap (Used)** | High-Traffic Carts | **No Global Lock**. Multiple threads can write to different segments simultaneously. | Slightly higher memory footprint than `HashMap`. |
| **Synchronized HashMap** | Simple Multi-threading | Easy to implement. | **Performance Bottleneck**. Only one user can modify *any* part of the cart at a time. |
| **Hashtable** | Legacy Systems | Thread-safe. | Obsolete; uses a single lock for the entire map, killing performance. |
| **CopyOnWriteArrayList** | Small Playlists/Configs| Thread-safe iteration. | **Extremely Slow Writes**. Every item addition requires copying the entire underlying array. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ ConcurrentHashMap (Lock-Striping)

``` java
private final ConcurrentHashMap<String, CartItem> items = new ConcurrentHashMap<>();
```

**Why this instead of `Collections.synchronizedMap()`?**
-   **Granular Locking**: `ConcurrentHashMap` divides the map into segments. A thread adding an item to "Bucket A" does not block a thread adding an item to "Bucket B". 
-   **Atomic Computed Updates**: We use `items.compute()` to update quantities. This ensures that the logic "get old quantity -> add new -> set quantity" happens as a single atomic unit, preventing race conditions where two threads update the same item.

## 2️⃣ AtomicReference

``` java
private final AtomicReference<Double> total = new AtomicReference<>(0.0);
```

**Why not just `double total`?**
-   **Race Conditions**: `total = total + price` is not atomic. In a multi-threaded environment, two updates could read the same "old total" and one would overwrite the other.
-   **CAS (Compare-And-Swap)**: `total.updateAndGet()` uses CPU-level atomic instructions to update the value without using heavy `synchronized` blocks. This is faster and more scalable.

## 3️⃣ Concurrent KeySet

``` java
private final Set<String> wishlist = ConcurrentHashMap.newKeySet();
```

**Why not `HashSet`?**
-   `HashSet` is not thread-safe. If one thread checks `contains()` while another `adds()`, it can throw a `ConcurrentModificationException` or enter an infinite loop. Using the `newKeySet()` view of a concurrent map provides a robust, thread-safe set.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structures Involved | Time Complexity |
| :--- | :--- | :--- |
| `addItem()` | ConcurrentHashMap + AtomicRef | **O(1)** |
| `updateQuantity()` | ConcurrentHashMap (Compute) | **O(1)** |
| `applyDiscount()` | ConcurrentHashMap Lookup | **O(1)** |
| `isInWishlist()` | Concurrent Set (Hashing) | **O(1)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Why use `AtomicReference<Double>` instead of `DoubleAdder`?
While `DoubleAdder` is generally faster for just adding numbers, `AtomicReference` allows us to perform more complex updates and ensures we always have a consistent state that can be easily retrieved and applied to discount logic.

### Memory Locality vs. Concurrency
Concurrent structures use more memory (locks, internal nodes, etc.) than their non-concurrent counterparts. For a single-user application, this is wasted overhead. However, for a backend service (where `ShoppingCart` might be managed in a shared session cache), the concurrency benefits far outweigh the memory cost.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Lock-Free Programming**: Using atomic variables to manage state transitions without deadlocks.
-   **Thread-Safe Autocomplete**: Using concurrent sets for instant, collision-free membership checks.
-   **Composite Atomicity**: Using the `compute` family of methods to perform multiple logical steps on a map entry in a single thread-safe operation.

------------------------------------------------------------------------

# Conclusion

The `ShoppingCart` implementation showcases modern Java concurrency at its best. By avoiding the 1990s-style "lock everything" approach and instead utilizing **Lock-Striping** and **Atomic Instructions**, we create a system that remains fast and consistent even under extreme multi-threaded pressure.
