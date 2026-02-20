package org.motadata.exercises.Day6.optimized;

import java.util.*;

public class ChatService {

    // Registered users
    private final Map<String, User> users = new HashMap<>();

    // Online users
    private final Set<String> onlineUsers = new HashSet<>();

    // conversationId -> Conversation
    private final Map<String, Conversation> conversations = new HashMap<>();

    public boolean register(String username, String password) {
        if (username == null || password == null || users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.password().equals(password)) {
            return false;
        }
        onlineUsers.add(username);
        return true;
    }

    public void logout(String username) {
        onlineUsers.remove(username);
    }

    public boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }

    private String getConversationId(String user1, String user2) {
        if (user1.compareTo(user2) < 0) {
            return user1 + "|" + user2;  // safer separator
        }
        return user2 + "|" + user1;
    }

    private Conversation getOrCreateConversation(String user1, String user2) {
        String id = getConversationId(user1, user2);

        return conversations.computeIfAbsent(id, key -> {
            Conversation conv = new Conversation(id);
            conv.getParticipants().add(user1);
            conv.getParticipants().add(user2);
            conv.initializeUserQueues(user1, user2);
            return conv;
        });
    }

    public void sendMessage(String from, String to, String content) {

        if (!users.containsKey(from) || !users.containsKey(to)) {
            throw new IllegalArgumentException("Sender or receiver does not exist");
        }

        if (!onlineUsers.contains(from)) {
            throw new IllegalStateException("Sender must be online");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }

        Conversation conv = getOrCreateConversation(from, to);

        Message message = new Message(from, content, System.currentTimeMillis());

        // Add to history
        conv.getMessageHistory().add(message);

        // Add to receiver's pending queue
        conv.getPendingMessages().get(to).offer(message);

        // Add to sender undo stack
        conv.getUndoStackPerUser()
                .computeIfAbsent(from, k -> new ArrayDeque<>())
                .push(message);

        // Index words
        String normalized = normalize(content);
        for (String word : normalized.split("\\s+")) {
            if (!word.isBlank()) {
                conv.getInvertedIndex()
                        .computeIfAbsent(word, k -> new HashSet<>())
                        .add(message);
            }
        }
    }

    public List<Message> receiveMessages(String user1, String user2) {

        String id = getConversationId(user1, user2);
        Conversation conv = conversations.get(id);

        if (conv == null) return Collections.emptyList();

        Deque<Message> queue = conv.getPendingMessages().get(user1);
        if (queue == null) return Collections.emptyList();

        List<Message> received = new ArrayList<>();

        while (!queue.isEmpty()) {
            received.add(queue.poll());
        }

        return received;
    }

    public boolean undoLastMessage(String from, String to) {

        String id = getConversationId(from, to);
        Conversation conv = conversations.get(id);

        if (conv == null) return false;

        Deque<Message> stack = conv.getUndoStackPerUser().get(from);
        if (stack == null || stack.isEmpty()) return false;

        Message last = stack.pop();

        // Remove from history
        conv.getMessageHistory().remove(last);

        // Remove from receiver pending queue
        conv.getPendingMessages().get(to).remove(last);

        // Remove from inverted index
        String normalized = normalize(last.content());
        for (String word : normalized.split("\\s+")) {
            Set<Message> set = conv.getInvertedIndex().get(word);
            if (set != null) {
                set.remove(last);
                if (set.isEmpty()) {
                    conv.getInvertedIndex().remove(word);
                }
            }
        }

        return true;
    }

    public List<Message> searchMessages(String user1, String user2, String keyword) {

        String id = getConversationId(user1, user2);
        Conversation conv = conversations.get(id);

        if (conv == null) return Collections.emptyList();

        Set<Message> resultSet =
                conv.getInvertedIndex().get(normalize(keyword));

        if (resultSet == null) return Collections.emptyList();

        List<Message> result = new ArrayList<>(resultSet);
        result.sort(Comparator.comparingLong(Message::timestamp));
        return result;
    }

    private String normalize(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .trim();
    }
}
