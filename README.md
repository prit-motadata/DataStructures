## 🚀 DataStructures – Java DSA Exercises

[![Build Status](https://github.com/prit-motadata/DataStructures/actions/workflows/maven.yml/badge.svg)](https://github.com/prit-motadata/DataStructures/actions/workflows/maven.yml) [![Qodana](https://github.com/prit-motadata/DataStructures/actions/workflows/qodana_code_quality.yml/badge.svg)](https://github.com/prit-motadata/DataStructures/actions/workflows/qodana_code_quality.yml) [![codecov](https://codecov.io/gh/prit-motadata/DataStructures/branch/main/graph/badge.svg)](https://codecov.io/gh/prit-motadata/DataStructures)


## 📖 About
This is a **Maven-based Java project** dedicated to mastering **Data Structures and Algorithms (DSA)** through real-world system design. This project isn't just about implementing arrays and lists; it's about building scalable, maintainable, and "production-ready" services.

### ✨ Key Design Philosophies
- **SOLID Principles**: 
    - **Single Responsibility**: Every data structure and service has one clear job.
    - **Open/Closed**: The system is designed to allow new implementations (like a new `Map` type) without modifying existing service logic.
    - **Dependency Inversion**: Services depend on abstractions, not concrete implementations, facilitated by our factory layer.
- **Design Patterns**: 
    - **Factory Pattern**: Used extensively via `MapFactory` and `ListFactory` to decouple creation logic from business logic.
    - **Sliding Window**: Implemented for high-performance network buffering.
- **Performance Optimized**: Every implementation includes a deep dive into **Big O complexity**, memory locality, and thread-safety tradeoffs.
- **Test-Driven Excellence**: Comprehensive JUnit 5 suites ensure logical correctness and edge-case handling.

---

## 🛠️ Tech Stack
- **Java 21**
- **Maven** (build & dependency management)
- **JUnit Jupiter** (testing)

---

## 📦 Getting Started

From the project root (`DataStructures`):

### 1️⃣ Build & Run Tests
Run all tests:

```bash
mvn test
```

This will compile the project and execute all tests under `src/test/java`.

---

## 📂 Project Structure

```text
src/
 ├── main/java/org/motadata/
 │    ├── Main.java
 │    ├── datastructures/            # Core DSA Implementations
 │    │    ├── array/                # Fixed & Dynamic Arrays
 │    │    ├── hashmap/              # Chained & Hybrid HashMaps
 │    │    ├── heap/                 # Binary Heaps
 │    │    ├── linkedlist/           # Singly & Doubly Linked Lists
 │    │    ├── queue/                # Circular & Sliding Window Queues
 │    │    └── trie/                 # Radix Trees (Compressed Tries)
 │    ├── common/                    # Shared Utilities & Patterns
 │    │    └── factory/              # Creational patterns for DSA
 │    │         ├── list/            # List implementation factories
 │    │         └── map/             # Map implementation factories
 │    └── exercises/                 # Real-world Applications
 │         ├── Day1/                 # Seat Booking (Concurrent Arrays)
 │         ├── Day2/                 # Video Streaming (Circular Buffers)
 │         ├── Day3/                 # Playlist (Dynamic Arrays/Lists)
 │         ├── Day4/                 # Browser Tabs (LinkedHashMap)
 │         ├── Day5/                 # User Login (Radix Tree + Map)
 │         ├── Day6/                 # Chat System (Guided & Optimized)
 │         ├── Day8/                 # Library Catalog (Secondary Indexing)
 │         ├── Day9/                 # Shopping Cart (Concurrency/Atomics)
 │         ├── Day10/                # URL Shortener (Base62 + AtomicLong)
 │         └── Day11/                # Task Scheduler (Priority Blocking Queue)
 └── test/java/org/motadata/         # JUnit 5 Test Suite
```

Other files:

- `pom.xml` – Maven project configuration (Java 21, JUnit Jupiter).
- `target/` – Maven build output (generated).
- `.idea/` – IntelliJ IDEA configuration.
- `.gitignore` – Git ignore rules.

---

## 🧪 Example Workflow
- **Edit or add** a data structure / exercise in `src/main/java`.
- **Write or update** tests in `src/test/java`.
- **Run**:

```bash
mvn test
```

to verify everything still passes.
