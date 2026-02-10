package org.motadata.exercises.Day6;

import org.motadata.datastructures.array.ArrayDynamic;
import org.motadata.datastructures.hashmap.GenericBucketHashMap;
import org.motadata.datastructures.hashmap.Map;
import org.motadata.datastructures.linkedlist.SinglyLinkedList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class ChatService {

    // Users → HashMap
    private final Map<String, String> users = new GenericBucketHashMap<>();

    // User → Index
    private final Map<String, Integer> userIndexMap = new GenericBucketHashMap<>();

    // Status → Array
    private final ArrayDynamic<UserStatus> statuses = new ArrayDynamic<>();

    // Message history → LinkedList
    private final SinglyLinkedList<Message> messageHistory = new SinglyLinkedList<>();

    // Message queue → Queue
    private final Queue<Message> messageQueue = new ArrayDeque<>();

    // Undo → Stack
    private final Deque<Message> undoStack = new ArrayDeque<>();

    private int userCount = 0;

    // ---------------- USER MANAGEMENT ----------------

    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }

        users.put(username, password);
        userIndexMap.put(username, userCount);
        statuses.add(UserStatus.OFFLINE);
        userCount++;

        return true;
    }

    public boolean login(String username, String password) {
        String stored = users.get(username);
        if (stored == null || !stored.equals(password)) {
            return false;
        }
        setStatus(username, UserStatus.ONLINE);
        return true;
    }

    public boolean logout(String username) {
        Integer index = userIndexMap.get(username);
        if (index == null) {
            return false;
        }

        if (statuses.get(index) == UserStatus.OFFLINE) {
            return false; // already logged out
        }

        setStatus(username, UserStatus.OFFLINE);
        return true;
    }

    private void setStatus(String username, UserStatus status) {
        Integer index = userIndexMap.get(username);
        if (index != null) {
            statuses.set(index, status);
        }
    }

    // ---------------- MESSAGING ----------------

    public void sendMessage(String from, String to, String content) {
        Message message = new Message(from, to, content);
        messageQueue.offer(message);
        undoStack.push(message);
    }

    public Message receiveMessage() {
        Message msg = messageQueue.poll();
        if (msg != null) {
            messageHistory.addLast(msg);
        }
        return msg;
    }

    // ---------------- UNDO ----------------

    public Message undoLastMessage() {
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.pop();
    }

    // ---------------- DISPLAY ----------------

    public void displayMessageHistory() {
        System.out.println("Chat History:");
        messageHistory.display();
    }
}