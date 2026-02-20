package org.motadata.exercises.Day6.optimized;

import java.util.*;

public class Conversation {

    private final String conversationId;

    private final Set<String> participants = new HashSet<>();

    private final List<Message> messageHistory = new ArrayList<>();

    private final Map<String, Deque<Message>> pendingMessages = new HashMap<>();

    private final Map<String, Deque<Message>> undoStackPerUser = new HashMap<>();

    private final Map<String, Set<Message>> invertedIndex = new HashMap<>();

    public Conversation(String conversationId) {
        this.conversationId = conversationId;
    }

    public void initializeUserQueues(String user1, String user2) {
        pendingMessages.put(user1, new ArrayDeque<>());
        pendingMessages.put(user2, new ArrayDeque<>());
    }

    public String getConversationId() {
        return conversationId;
    }

    public Set<String> getParticipants() { return participants; }

    public List<Message> getMessageHistory() { return messageHistory; }

    public Map<String, Deque<Message>> getPendingMessages() {
        return pendingMessages;
    }

    public Map<String, Deque<Message>> getUndoStackPerUser() { return undoStackPerUser; }

    public Map<String, Set<Message>> getInvertedIndex() {
        return invertedIndex;
    }
}
