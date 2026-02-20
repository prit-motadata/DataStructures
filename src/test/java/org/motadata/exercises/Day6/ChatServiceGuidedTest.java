package org.motadata.exercises.Day6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.motadata.exercises.Day6.guided.ChatService;
import org.motadata.exercises.Day6.guided.Message;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceGuidedTest {

    private ChatService chatService;
    private java.io.ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        chatService = new ChatService();

        outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
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

    @Test
    void searchMessages_nullKeyword_printsInvalid() {
        chatService.searchMessages(null);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Invalid keyword"));
    }

    @Test
    void searchMessages_emptyKeyword_printsInvalid() {
        chatService.searchMessages("   ");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Invalid keyword"));
    }

    @Test
    void searchMessages_noMatch_printsNoMessagesFound() {
        chatService.register("a", "1");
        chatService.register("b", "2");
        chatService.login("a", "1");

        chatService.sendMessage("a", "b", "Hello World");
        chatService.receiveMessage("b");

        chatService.searchMessages("xyz");

        String output = outContent.toString();
        assertTrue(output.contains("Search Results for: xyz"));
        assertTrue(output.contains("No messages found."));
    }

    @Test
    void searchMessages_shouldDisplayMatchingMessages() {
        chatService.register("a", "1");
        chatService.register("b", "2");
        chatService.login("a", "1");

        chatService.sendMessage("a", "b", "Hello Java");
        chatService.sendMessage("a", "b", "Spring Boot Rocks");

        chatService.receiveMessage("b");
        chatService.receiveMessage("b");

        chatService.searchMessages("java");

        String output = outContent.toString().toLowerCase();

        assertTrue(output.contains("search results for: java"));
        assertTrue(output.contains("hello java"));
        assertFalse(output.contains("spring boot rocks"));
    }

    @Test
    void searchMessages_shouldBeCaseInsensitive() {
        chatService.register("a", "1");
        chatService.register("b", "2");
        chatService.login("a", "1");

        chatService.sendMessage("a", "b", "HELLO WORLD");
        chatService.receiveMessage("b");

        chatService.searchMessages("hello");

        String output = outContent.toString().toLowerCase();
        assertTrue(output.contains("hello world"));
    }
}
