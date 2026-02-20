# In-Memory Chat Service (Data Structure Focused)

## Overview

This project implements a **multi-user in-memory chat backend** using
only core Java data structures. The goal is to practice selecting the
*right* data structure based on workload patterns and logical
requirements.

The system supports:

-   User registration
-   Login / Logout
-   Online status tracking
-   1-to-1 messaging
-   Per-user message delivery queues
-   Undo last message (per user)
-   Keyword-based search using an inverted index

------------------------------------------------------------------------

# Architecture Design

The system is centered around a `Conversation` object.

    ChatService
     ├── users (Map)
     ├── onlineUsers (Set)
     └── conversations (Map)
            └── Conversation
                  ├── participants (Set)
                  ├── messageHistory (ArrayList)
                  ├── pendingMessages (Map → Queue)
                  ├── undoStackPerUser (Map → Stack)
                  └── invertedIndex (Map → Set)

------------------------------------------------------------------------

# Data Structures Used and Why

## 1️⃣ Users Storage

``` java
Map<String, User> users
```

**Why HashMap?**

-   O(1) average lookup
-   Fast login validation
-   Direct username-based access
-   Ideal for key-value identity mapping

------------------------------------------------------------------------

## 2️⃣ Online Users

``` java
Set<String> onlineUsers
```

**Why HashSet?**

-   O(1) membership check
-   Ensures uniqueness
-   Fast add/remove
-   Perfect for tracking active sessions

------------------------------------------------------------------------

## 3️⃣ Conversations

``` java
Map<String, Conversation> conversations
```

**Why HashMap?**

-   Conversation ID → direct access
-   O(1) average lookup
-   Clean separation per conversation

------------------------------------------------------------------------

## 4️⃣ Participants

``` java
Set<String> participants
```

**Why HashSet?**

-   Prevent duplicate users
-   O(1) membership validation
-   Ideal for access control checks

------------------------------------------------------------------------

## 5️⃣ Message History

``` java
List<Message> messageHistory = new ArrayList<>()
```

**Why ArrayList instead of LinkedList or Tree?**

-   Append-heavy workload → O(1) amortized
-   Sequential iteration is fast (cache-friendly)
-   Lower memory overhead than LinkedList
-   Messages are naturally inserted in order
-   No need for rebalancing (TreeMap unnecessary)

------------------------------------------------------------------------

## 6️⃣ Per-User Pending Messages

``` java
Map<String, Deque<Message>> pendingMessages
```

**Why Map + ArrayDeque?**

-   Each user has independent delivery queue
-   O(1) enqueue and dequeue
-   Avoids global queue draining bug
-   Correct multi-user semantics

------------------------------------------------------------------------

## 7️⃣ Undo Stack (Per User)

``` java
Map<String, Deque<Message>> undoStackPerUser
```

**Why Stack (ArrayDeque)?**

-   Undo is naturally LIFO
-   O(1) push/pop
-   Avoid scanning history (which would be O(n))
-   Ensures per-user undo correctness

------------------------------------------------------------------------

## 8️⃣ Inverted Index for Search

``` java
Map<String, Set<Message>> invertedIndex
```

**Why HashMap + HashSet?**

-   Keyword → messages lookup in O(1) average
-   Avoid full O(n) history scans
-   Efficient for repeated searches
-   Set prevents duplicate indexing

------------------------------------------------------------------------

# Time Complexity Table

  -----------------------------------------------------------------------
  Operation       Data Structures Involved           Time Complexity
  --------------- ---------------------------------- --------------------
  Register        HashMap                            O(1)

  Login           HashMap + HashSet                  O(1)

  Logout          HashSet                            O(1)

  Send Message    ArrayList + Queue + Stack + Index  O(k) (k = words in
                                                     message)

  Receive         ArrayDeque                         O(m) (m = unread
  Messages                                           messages)

  Undo Last       Stack + ArrayList + Index          O(k)
  Message                                            

  Search          Inverted Index                     O(1) average
  (keyword)                                          

  Conversation    HashMap                            O(1)
  Lookup                                             
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# Design Tradeoffs

### Why not LinkedList for message history?

-   Higher memory overhead
-   Slower iteration
-   Poor cache locality

### Why not TreeMap?

-   O(log n) insert
-   No benefit when messages are already chronological
-   Extra memory overhead

### Why not scan ArrayList for search?

-   Would be O(n)
-   Inverted index reduces it to O(1) average

------------------------------------------------------------------------

# Key Design Principles Demonstrated

-   Choose data structure based on access pattern
-   Optimize reads vs writes based on workload
-   Maintain auxiliary indexes for performance
-   Separate concerns (conversation as aggregate root)
-   Use correct abstraction (Stack for undo, Queue for delivery)

------------------------------------------------------------------------

# Conclusion

This project demonstrates how to design a logically correct, efficient
in-memory chat system using:

-   HashMap
-   HashSet
-   ArrayList
-   ArrayDeque

It emphasizes reasoning about: - Time complexity - Memory tradeoffs -
Correct multi-user semantics - Data structure appropriateness

This is not production infrastructure, but a strong
data-structure-driven system design exercise.
