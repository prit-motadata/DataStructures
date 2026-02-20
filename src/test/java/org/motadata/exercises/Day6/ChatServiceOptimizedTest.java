package org.motadata.exercises.Day6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.motadata.exercises.Day6.optimized.ChatService;
import org.motadata.exercises.Day6.optimized.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceOptimizedTest {

    private ChatService chatService;

    @BeforeEach
    void setup() {
        chatService = new ChatService();
    }

    // =========================
    // REGISTER TESTS
    // =========================

    @Test
    void shouldRegisterUserSuccessfully() {
        assertTrue(chatService.register("alice", "123"));
    }

    @Test
    void shouldNotRegisterDuplicateUser() {
        chatService.register("alice", "123");
        assertFalse(chatService.register("alice", "456"));
    }

    @Test
    void shouldNotRegisterNullUser() {
        assertFalse(chatService.register(null, "123"));
        assertFalse(chatService.register("bob", null));
    }

    // =========================
    // LOGIN TESTS
    // =========================

    @Test
    void shouldLoginSuccessfully() {
        chatService.register("alice", "123");
        assertTrue(chatService.login("alice", "123"));
        assertTrue(chatService.isOnline("alice"));
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        chatService.register("alice", "123");
        assertFalse(chatService.login("alice", "wrong"));
    }

    @Test
    void shouldFailLoginIfUserNotExists() {
        assertFalse(chatService.login("ghost", "123"));
    }

    @Test
    void shouldLogoutUser() {
        chatService.register("alice", "123");
        chatService.login("alice", "123");
        chatService.logout("alice");
        assertFalse(chatService.isOnline("alice"));
    }

    // =========================
    // SEND MESSAGE TESTS
    // =========================

    @Test
    void shouldSendAndReceiveMessage() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hello Bob");

        List<Message> received = chatService.receiveMessages("bob", "alice");

        assertEquals(1, received.size());
        assertEquals("Hello Bob", received.getFirst().content());
    }

    @Test
    void shouldThrowIfSenderOffline() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        assertThrows(IllegalStateException.class,
                () -> chatService.sendMessage("alice", "bob", "Hi"));
    }

    @Test
    void shouldThrowIfReceiverNotExists() {
        chatService.register("alice", "123");
        chatService.login("alice", "123");

        assertThrows(IllegalArgumentException.class,
                () -> chatService.sendMessage("alice", "ghost", "Hi"));
    }

    @Test
    void shouldThrowIfSelfMessage() {
        chatService.register("alice", "123");
        chatService.login("alice", "123");

        assertThrows(IllegalArgumentException.class,
                () -> chatService.sendMessage("alice", "alice", "Hi"));
    }

    // =========================
    // UNDO TESTS
    // =========================

    @Test
    void shouldUndoLastMessage() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hello");
        assertTrue(chatService.undoLastMessage("alice", "bob"));

        List<Message> received = chatService.receiveMessages("bob", "alice");
        assertTrue(received.isEmpty());
    }

    @Test
    void shouldFailUndoIfNoConversation() {
        assertFalse(chatService.undoLastMessage("alice", "bob"));
    }

    @Test
    void shouldFailUndoIfNoMessages() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");
        chatService.login("alice", "123");

        assertFalse(chatService.undoLastMessage("alice", "bob"));
    }

    // =========================
    // SEARCH TESTS
    // =========================

    @Test
    void shouldSearchMessagesSuccessfully() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hello Bob");
        chatService.sendMessage("alice", "bob", "How are you");

        List<Message> results = chatService.searchMessages("alice", "bob", "hello");

        assertEquals(1, results.size());
        assertEquals("Hello Bob", results.getFirst().content());
    }

    @Test
    void shouldReturnEmptyIfSearchNotFound() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hello");

        List<Message> results = chatService.searchMessages("alice", "bob", "xyz");

        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfNoConversation() {
        List<Message> results = chatService.searchMessages("a", "b", "test");
        assertTrue(results.isEmpty());
    }
}