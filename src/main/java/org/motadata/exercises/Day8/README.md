# Library Catalog (Secondary Indexing Focused)

## Overview

This project implements an **In-Memory Library Catalog** that allows for high-performance retrieval of books using multiple identifiers. It focuses on the concept of **Indexing**, providing constant-time lookups for both unique IDs (ISBN) and descriptive fields (Title).

The system supports:
- **Primary Key Lookups**: Instant retrieval via unique ISBN.
- **Secondary Indexing**: Fast case-insensitive search by book title without scanning the entire database.
- **Data Integrity**: Ensures that adding or removing a book updates all indexes atomically.

------------------------------------------------------------------------

# Architecture Design

The service maintains two independent hash maps to serve as indexes for the same underlying set of `Book` objects.

    LibraryCatalog
     ├── booksByIsbn (Map<String, Book>) - Primary index for O(1) ISBN lookup
     └── booksByTitle (Map<String, Book>) - Secondary index for O(1) title lookup (lowercased)

------------------------------------------------------------------------

# Data Structures Comparison

| Data Structure | Use Case in Catalog | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **HashMap (Used)** | Fast Book Retrieval | **O(1) Average Lookup** by both ISBN and Title. | Higher memory usage due to dual maps. |
| **ArrayList** | Simple Storage | Memory efficient. | **O(N) Search**. Finding a book by title requires checking every single entry. |
| **TreeMap** | Categorized Sorting | Keeps books sorted by title/ISBN automatically. | **O(log N) operations**, which is slower than hashing. |
| **HashSet** | Unique Catalog | Ensures no duplicates. | Cannot retrieve a book object by its title or ISBN; can only check if it "exists". |

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ Secondary Indexing (Dual HashMap)

``` java
private final Map<String, Book> booksByIsbn = new HashMap<>();
private final Map<String, Book> booksByTitle = new HashMap<>();
```

**Why Dual HashMaps?**
-   **No More Scans**: In a library with 1,000,000 books, finding a title in an `ArrayList` would take 1,000,000 checks in the worst case. With a secondary `HashMap`, it takes **exactly 1 check**.
-   **O(1) Everything**: By trading off memory (storing references to the same object in two maps), we ensure that all primary user actions—searching by ID or searching by Name—are equally fast.
-   **Normalization**: The `booksByTitle` map uses lowercased keys. This allows for robust, case-insensitive searching ("The Great Gatsby" vs "the great gatsby") while still providing O(1) performance.

------------------------------------------------------------------------

# Time Complexity Table

| Operation | Data Structure Involved | Time Complexity |
| :--- | :--- | :--- |
| `addBook()` | Dual HashMap Insert | **O(1)** (Average) |
| `removeBook()` | Dual HashMap Remove | **O(1)** (Average) |
| `searchByIsbn()` | Primary Index Lookup | **O(1)** |
| `searchByTitle()` | Secondary Index Lookup | **O(1)** |

------------------------------------------------------------------------

# Design Tradeoffs

### Memory vs. Performance
The primary tradeoff here is **Space for Time**. We are storing the pointers to book objects twice. However, in modern computing, the cost of extra RAM is almost always preferred over the high latency of O(N) searches in an application that is read-heavy (like a catalog).

### Consistency Complexity
By having two indexes, every "write" operation (`addBook`, `removeBook`) becomes twice as complex to maintain. If we remove a book from the ISBN map but forget the Title map, we create a **stale index** or **memory leak**. The implementation ensures that both maps are updated synchronously.

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   **Case Insensitivity Mapping**: Normalizing data (lowercasing) before it enters a hash-based index to enable intuitive user search.
-   **Atomic Index Updates**: Ensuring that a data object is either fully indexed or not at all, preventing partial state bugs.
-   **Index-Driven Retrieval**: Moving away from "find by scanning" to "find by mapping", which is the foundation of high-scale database engines.

------------------------------------------------------------------------

# Conclusion

The `LibraryCatalog` illustrates how professional-grade databases and search engines optimize for diverse queries. By layering multiple **Hash Maps** over a single dataset, we create a tool that stays lightning-fast regardless of whether the collection has 10 books or 10 million.
