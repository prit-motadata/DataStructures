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

    // Message queue → Queue (shared across all users)
    private final Queue<Message> messageQueue = new ArrayDeque<>();

    // Per-user undo → Stack
    private final Map<String, Deque<Message>> userUndoStacks = new GenericBucketHashMap<>();

    private int userCount = 0;

    // ---------------- USER MANAGEMENT ----------------

    public boolean register(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        if (users.containsKey(username)) {
            return false;
        }

        users.put(username, password);
        userIndexMap.put(username, userCount);
        statuses.add(UserStatus.OFFLINE);
        userUndoStacks.put(username, new ArrayDeque<>());
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

    public boolean sendMessage(String from, String to, String content) {
        if (!users.containsKey(from) || !users.containsKey(to)) {
            return false;
        }

        Integer senderIndex = userIndexMap.get(from);
        if (statuses.get(senderIndex) != UserStatus.ONLINE) {
            return false;
        }

        Message message = new Message(from, to, content);
        messageQueue.offer(message);

        Deque<Message> undoStack = userUndoStacks.get(from);
        if (undoStack != null) {
            undoStack.push(message);
        }
        return true;
    }

    /**
     * Receive the next message for a specific user.
     * This scales for multiple users by only delivering messages addressed to {@code username}.
     */
    public Message receiveMessage(String username) {
        if (username == null || !users.containsKey(username)) {
            return null;
        }

        if (messageQueue.isEmpty()) {
            return null;
        }

        int size = messageQueue.size();
        Message target = null;

        // Rotate through the queue once to find the first message for this user
        for (int i = 0; i < size; i++) {
            Message msg = messageQueue.poll();
            if (target == null && msg.to().equals(username)) {
                target = msg;
            } else {
                messageQueue.offer(msg);
            }
        }

        if (target != null) {
            messageHistory.addLast(target);
        }

        return target;
    }

    // ---------------- UNDO ----------------

    /**
     * Undo the last message sent by a specific user.
     * This only affects messages sent by {@code username}, allowing multiple users to undo independently.
     */
    public Message undoLastMessage(String username) {
        if (username == null) {
            return null;
        }

        Deque<Message> undoStack = userUndoStacks.get(username);
        if (undoStack == null || undoStack.isEmpty()) {
            return null;
        }

        Message last = undoStack.pop();

        messageQueue.remove(last);

        return last;
    }

    // ---------------- SEARCH ----------------

    public void searchMessages(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Invalid keyword");
            return;
        }

        System.out.println("Search Results for: " + keyword);

        SinglyLinkedList<Message> results =
                messageHistory.search(msg ->
                        msg.content().toLowerCase()
                                .contains(keyword.toLowerCase()));

        if (results.isEmpty()) {
            System.out.println("No messages found.");
            return;
        }

        results.display();
    }

    // ---------------- DISPLAY ----------------

    public void displayMessageHistory() {
        System.out.println("Chat History:");
        messageHistory.display();
    }
}