package org.motadata.datastructures.linkedlist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.motadata.common.factory.list.ListFactory;
import org.motadata.common.factory.list.ListType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListImplementationTest {

    private <T> List<T> createList(ListType type) {
        return ListFactory.createList(type);
    }

    // -------- ADD FIRST --------

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldAddFirst(ListType type) {
        List<String> list = createList(type);

        list.addFirst("B");
        list.addFirst("A");

        assertTrue(list.contains("A"));
        assertTrue(list.contains("B"));
        assertFalse(list.isEmpty());
    }

    // -------- ADD LAST --------

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldAddLast(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("B");

        assertTrue(list.contains("A"));
        assertTrue(list.contains("B"));
    }

    // -------- REMOVE --------

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldRemoveHead(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("B");

        assertTrue(list.remove("A"));
        assertFalse(list.contains("A"));
        assertTrue(list.contains("B"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldRemoveMiddle(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        assertTrue(list.remove("B"));
        assertFalse(list.contains("B"));
        assertTrue(list.contains("A"));
        assertTrue(list.contains("C"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldRemoveTail(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("B");

        assertTrue(list.remove("B"));
        assertFalse(list.contains("B"));
        assertTrue(list.contains("A"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldReturnFalseWhenRemovingMissingElement(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");

        assertFalse(list.remove("Z"));
    }

    // -------- CONTAINS --------

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldReturnTrueIfElementExists(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");

        assertTrue(list.contains("A"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldReturnFalseIfElementDoesNotExist(ListType type) {
        List<String> list = createList(type);

        assertFalse(list.contains("A"));
    }

    // -------- EMPTY --------

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldBeEmptyInitially(ListType type) {
        List<String> list = createList(type);

        assertTrue(list.isEmpty());
    }

    // -------- DOUBLY SPECIFIC --------

    @ParameterizedTest
    @EnumSource(value = ListType.class, names = "DOUBLY")
    void shouldAddBefore(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("C");

        DoublyLinkedList<String> doubly =
                (DoublyLinkedList<String>) list;

        assertTrue(doubly.addBefore("C", "B"));

        assertTrue(list.contains("B"));
    }

    @ParameterizedTest
    @EnumSource(value = ListType.class, names = "DOUBLY")
    void shouldAddAfter(ListType type) {
        List<String> list = createList(type);

        list.addLast("A");
        list.addLast("B");

        DoublyLinkedList<String> doubly =
                (DoublyLinkedList<String>) list;

        assertTrue(doubly.addAfter("A", "X"));

        assertTrue(list.contains("X"));
    }

    @ParameterizedTest
    @EnumSource(value = ListType.class, names = "DOUBLY")
    void shouldReturnFalseWhenAddBeforeNotFound(ListType type) {
        List<String> list = createList(type);

        DoublyLinkedList<String> doubly =
                (DoublyLinkedList<String>) list;

        assertFalse(doubly.addBefore("Z", "A"));
    }

    @ParameterizedTest
    @EnumSource(value = ListType.class, names = "DOUBLY")
    void shouldReturnFalseWhenAddAfterNotFound(ListType type) {
        List<String> list = createList(type);

        DoublyLinkedList<String> doubly =
                (DoublyLinkedList<String>) list;

        assertFalse(doubly.addAfter("Z", "A"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldSearchUsingPredicate(ListType type) {
        List<Integer> list = ListFactory.createList(type);

        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);

        List<Integer> even = list.search(n -> n % 2 == 0);

        assertTrue(even.contains(2));
        assertTrue(even.contains(4));
        assertFalse(even.contains(1));
        assertFalse(even.contains(3));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldDisplayListCorrectly(ListType type) {
        List<String> list = ListFactory.createList(type);

        list.addLast("A");
        list.addLast("B");

        // Capture System.out
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        list.display();

        String output = out.toString().trim();

        assertTrue(output.contains("A"));
        assertTrue(output.contains("B"));
    }

    @ParameterizedTest
    @EnumSource(ListType.class)
    void shouldDisplayEmptyMessageWhenListIsEmpty(ListType type) {
        List<String> list = ListFactory.createList(type);

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        list.display();

        String output = out.toString().trim();

        assertTrue(output.contains("empty"));
    }

    // -------- DEFAULT FACTORY --------

    @Test
    void shouldCreateSinglyListByDefault() {
        List<String> list = ListFactory.createDefault();

        list.addLast("A");

        assertTrue(list.contains("A"));
    }

    @ParameterizedTest
    @EnumSource(value = ListType.class, names = "DOUBLY")
    void shouldDisplayReverseCorrectly(ListType type) {
        List<String> list = ListFactory.createList(type);

        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        BidirectionalList<String> bidirectional =
                (BidirectionalList<String>) list;

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        bidirectional.displayReverse();

        String output = out.toString().trim();

        assertTrue(output.contains("C"));
        assertTrue(output.contains("B"));
        assertTrue(output.contains("A"));
    }

}
