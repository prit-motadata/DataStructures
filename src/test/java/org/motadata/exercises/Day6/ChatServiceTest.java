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

        Message received = chatService.receiveMessage("bob");
        assertNotNull(received);
        assertEquals("alice", received.from());
        assertEquals("bob", received.to());
        assertEquals("Hello", received.content());
    }

    @Test
    void receiveMessageWhenQueueEmpty_returnsNull() {
        assertNull(chatService.receiveMessage("any"));
    }

    // ---------------- UNDO ----------------

    @Test
    void undoLastMessage_success() {
        chatService.register("alice", "123");
        chatService.register("bob", "456");
        chatService.login("alice", "123");

        chatService.sendMessage("alice", "bob", "Hi");

        Message undone = chatService.undoLastMessage("alice");
        assertNotNull(undone);
        assertEquals("Hi", undone.content());
    }

    @Test
    void undoWhenStackEmpty_returnsNull() {
        assertNull(chatService.undoLastMessage("alice"));
    }

    @Test
    void multiUserMessaging_deliversMessagesToCorrectRecipients() {
        chatService.register("alice", "1");
        chatService.register("bob", "2");
        chatService.register("charlie", "3");

        chatService.login("alice", "1");
        chatService.login("charlie", "3");

        assertTrue(chatService.sendMessage("alice", "bob", "Hi Bob from Alice"));
        assertTrue(chatService.sendMessage("charlie", "bob", "Hi Bob from Charlie"));

        Message firstForBob = chatService.receiveMessage("bob");
        Message secondForBob = chatService.receiveMessage("bob");

        assertNotNull(firstForBob);
        assertNotNull(secondForBob);
        assertEquals("alice", firstForBob.from());
        assertEquals("charlie", secondForBob.from());
    }

    @Test
    void undoIsPerUser_onlyAffectsSendersMessages() {
        chatService.register("alice", "1");
        chatService.register("bob", "2");
        chatService.register("charlie", "3");

        chatService.login("alice", "1");
        chatService.login("charlie", "3");

        // Alice and Charlie both send messages to Bob
        chatService.sendMessage("alice", "bob", "From Alice");
        chatService.sendMessage("charlie", "bob", "From Charlie");

        // Alice undoes her last message
        Message undone = chatService.undoLastMessage("alice");
        assertNotNull(undone);
        assertEquals("From Alice", undone.content());

        // Bob should only receive Charlie's message now
        Message forBob = chatService.receiveMessage("bob");
        assertNotNull(forBob);
        assertEquals("charlie", forBob.from());
        assertEquals("From Charlie", forBob.content());
    }

    // ---------------- HISTORY ----------------

    @Test
    void messageAddedToHistoryAfterReceive() {
        chatService.register("a", "1");
        chatService.register("b", "2");
        chatService.login("a", "1");

        boolean sent = chatService.sendMessage("a", "b", "msg1");
        assertTrue(sent);

        chatService.receiveMessage("b");

        // Just ensure no exception and history path executed
        chatService.displayMessageHistory();
    }
}
