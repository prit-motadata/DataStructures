# Concurrent Task Scheduler (Priority Queue Focused)

## Overview

This project implements a **Multi-threaded Task Scheduler** that processes background jobs based on their priority. It demonstrates how to combine concurrent collections with thread pools to build a producer-consumer system where tasks are consumed in order of importance rather than just arrival time.

The system supports:
- **Priority-Based Execution**: Higher priority tasks jump to the front of the queue.
- **Worker Thread Pool**: Uses multiple background threads to process tasks in parallel.
- **Thread-Safe Queuing**: Utilizes `PriorityBlockingQueue` for thread-safe coordination between producers (scheduling) and consumers (workers).
- **Graceful Shutdown**: Ensures all pending tasks are processed or timeout correctly during system exit.

------------------------------------------------------------------------

# Architecture Design

The scheduler uses a classic **Producer-Consumer** pattern with a priority-sorted buffer.

    TaskSchedulerConcurrent
     ├── queue (PriorityBlockingQueue<Task>) - Ordered by priority (max-heap)
     └── workers (ExecutorService) - Pool of background threads
           └── processTasks() - Continuous worker loop

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Scheduling | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **PriorityBlockingQueue (Used)** | Multi-threaded Scheduler | **O(log N) Priority Insert + O(log N) poll**. Thread-safe without manual locks. | Iteration is unordered; requires binary heap maintenance. |
| **ConcurrentLinkedQueue** | Standard FIFO Scheduler | O(1) Insert/Extract. | **No Priority support**; strictly First-In-First-Out. |
| **PriorityQueue** | Single-threaded Scheduler | Fast priority sorting. | **Not Thread-Safe**. Multiple threads adding/polling will crash the JVM or corrupt the heap. |
| **TreeSet** | Unique Task Sets | Keeps tasks uniquely sorted. | **O(log N) insertion**. Not ideal for "polling" (extracting) under high concurrency; doesn't handle duplicate priorities well. |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ PriorityBlockingQueue (The Buffer)

``` java
private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
```

**Why this instead of a normal `Queue`?**
-   **Heap-Based Ordering**: Internally uses a binary heap to ensure the `Task` with the highest `priority` is always at the head.
-   **Blocking Operations**: The `poll(timeout, unit)` method allows worker threads to "wait" for work without consuming CPU (sleep/wait), but wakes them up instantly when a new task is scheduled.
-   **Atomic Hand-off**: It handles all internal locking. When multiple producer threads call `scheduleTask` and multiple worker threads call `poll`, the queue ensures no two workers ever get the same task and no task is lost.

## 2️⃣ Fixed Thread Pool (Workers)

``` java
workers = Executors.newFixedThreadPool(workerCount);
```

**Why use a pool instead of `new Thread()`?**
-   **Resource Management**: Creating a thread is expensive. A pool reuses workers, saving memory and CPU cycles.
-   **Load Control**: By limiting the number of workers, we prevent the application from overwhelming the OS if thousands of tasks are suddenly scheduled.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structures Involved | Time Complexity |
| :--- | :--- | :--- |
| `scheduleTask()` | Priority Queue Insert | **O(log N)** |
| `processTasks()` (poll)| Priority Queue Extract | **O(log N)** |
| `pendingTasks()` | Queue Size | **O(1)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Why O(log N) instead of O(1)?
In a FIFO queue, adding an item is O(1). However, in a **Priority Scheduler**, we must maintain order. The logarithmic cost (O(log N)) is the mathematical price we pay for ensuring that "CRITICAL" tasks always run before "LOW" tasks, regardless of when they arrived.

### Volatile Flag for Shutdown
The `running` flag is marked as `volatile`. This ensures that when one thread (the main thread) calls `shutdown()`, all worker threads immediately see the change in value across their CPU caches, allowing for a synchronized and graceful exit.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Producer-Consumer Pattern**: Decoupling the creation of work from its execution.
-   **Heap Property Management**: Using `Comparable<Task>` to define custom priority logic (Max-Heap behavior).
-   **Thread Interruption**: Correctly handling `InterruptedException` to allow workers to exit cleanly during shutdown.

------------------------------------------------------------------------

# Conclusion

The `TaskSchedulerConcurrent` represents the engine room of many modern applications (like OS schedulers or background job processors). By combining the **Binary Heap** logic of priority queues with **Thread Pool** management, it creates a powerful system that balances fairness, urgency, and resource efficiency.
