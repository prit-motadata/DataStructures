package org.motadata.exercises.Day6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService();
    }

    // ---------------- REGISTER ----------------

    @Test
    void registerNewUser_success() {
        assertTrue(chatService.register("alice", "pass123"));
    }

    @Test
    void registerDuplicateUser_fails() {
        chatService.register("alice", "pass123");
        assertFalse(chatService.register("alice", "pass123"));
    }

    // ---------------- LOGIN ----------------

    @Test
    void loginWithCorrectCredentials_success() {
        chatService.register("bob", "secret");
        assertTrue(chatService.login("bob", "secret"));
    }

    @Test
    void loginWithWrongPassword_fails() {
        chatService.register("bob", "secret");
        assertFalse(chatService.login("bob", "wrong"));
    }

    @Test
    void loginNonExistingUser_fails() {
        assertFalse(chatService.login("ghost", "nope"));
    }

    // ---------------- LOGOUT ----------------

    @Test
    void logoutLoggedInUser_success() {
        chatService.register("charlie", "pwd");
        chatService.login("charlie", "pwd");

        assertTrue(chatService.logout("charlie"));
    }

    @Test
    void logoutAlreadyLoggedOutUser_fails() {
        chatService.register("charlie", "pwd");

        assertFalse(chatService.logout("charlie"));
    }

    @Test
    void logoutNonExistingUser_fails() {
        assertFalse(chatService.logout("nobody"));
    }

    // ---------------- MESSAGING ----------------

    @Test
    void sendAndReceiveMessage_success() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");

        chatService.login("alice", "123");

        boolean sent = chatService.sendMessage("alice", "bob", "Hello");
        assertTrue(sent);

        Message received = chatService.receiveMessage();
        assertNotNull(received);
        assertEquals("alice", received.from());
        assertEquals("bob", received.to());
        assertEquals("Hello", received.content());
    }

    @Test
    void receiveMessageWhenQueueEmpty_returnsNull() {
        assertNull(chatService.receiveMessage());
    }

    // ---------------- UNDO ----------------

    @Test
    void undoLastMessage_success() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");
        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hi");

        Message undone = chatService.undoLastMessage();
        assertNotNull(undone);
        assertEquals("Hi", undone.content());
    }

    @Test
    void undoWhenStackEmpty_returnsNull() {
        assertNull(chatService.undoLastMessage());
    }

    // ---------------- HISTORY ----------------

    @Test
    void messageAddedToHistoryAfterReceive() {
        chatService.register("a", "1");
        chatService.register("b", "2");
        chatService.login("a", "1");

        boolean sent = chatService.sendMessage("a", "b", "msg1");
        assertTrue(sent);

        chatService.receiveMessage();

        // Just ensure no exception and history path executed
        chatService.displayMessageHistory();
    }
}
