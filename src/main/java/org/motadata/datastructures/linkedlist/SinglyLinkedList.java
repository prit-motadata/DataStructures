package org.motadata.datastructures.linkedlist;

import java.util.function.Predicate;

/**
 * Singly linked list implementation that supports insertion, removal, search, and display operations.
 *
 * @param <T> element type stored in the list
 * @author prit.thakkar@motadata.com
 */
public class SinglyLinkedList<T> implements List<T> {

    private Node<T> head;
    private Node<T> tail;

    // Add at beginning

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }

    // Add at end

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = tail = newNode;
            return;
        }

        tail.next = newNode;
        tail = newNode;
    }

    // Remove element

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(T data) {
        if (head == null) {
            return false;
        }

        // Remove head
        if (head.data.equals(data)) {
            head = head.next;

            // If list becomes empty
            if (head == null) {
                tail = null;
            }
            return true;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {

                // If removing tail
                if (current.next == tail) {
                    tail = current;
                }

                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Search Element

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Display list

    /**
     * {@inheritDoc}
     */
    @Override
    public void display() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("END");
    }

    // Search list

    /**
        * {@inheritDoc}
        */
    @Override
    public SinglyLinkedList<T> search(Predicate<T> condition) {
        SinglyLinkedList<T> result = new SinglyLinkedList<>();

        Node<T> current = head;

        while (current != null) {
            if (condition.test(current.data)) {
                result.addLast(current.data);
            }
            current = current.next;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Internal node representing a single element in the list.
     *
     * @param <T> element type
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
