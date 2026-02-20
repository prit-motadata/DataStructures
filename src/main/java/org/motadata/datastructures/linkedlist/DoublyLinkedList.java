package org.motadata.datastructures.linkedlist;

import java.util.function.Predicate;

/**
 * Doubly linked list implementation that supports bidirectional traversal and position-based insertion.
 *
 * @param <T> element type stored in the list
 * @author prit.thakkar@motadata.com
 */
public class DoublyLinkedList<T> implements BidirectionalList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Node representing an element in the doubly linked list with references to neighbors.
     *
     * @param <T> element type
     */
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
        }
    }

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
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Add at end

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    // Remove element

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(T data) {
        if (head == null) return false;

        Node<T> current = head;

        while (current != null) {

            if (current.data.equals(data)) {

                // Removing head
                if (current == head) {
                    head = current.next;
                    if (head != null) {
                        head.prev = null;
                    }
                }

                // Removing tail
                if (current == tail) {
                    tail = current.prev;
                    if (tail != null) {
                        tail.next = null;
                    }
                }

                // Removing middle
                if (current.prev != null) {
                    current.prev.next = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    // Search element

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

    // Display forward

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
            System.out.print(current.data + " <-> ");
            current = current.next;
        }

        System.out.println("END");
    }

    // Display reverse (advantage of doubly list)

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayReverse() {
        if (tail == null) {
            System.out.println("List is empty");
            return;
        }

        Node<T> current = tail;

        while (current != null) {
            System.out.print(current.data + " <-> ");
            current = current.prev;
        }

        System.out.println("START");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoublyLinkedList<T> search(Predicate<T> condition) {
        DoublyLinkedList<T> result = new DoublyLinkedList<>();

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
        return size == 0;
    }

    /**
     * Returns the number of elements currently stored in this list.
     *
     * @return list size
     */
    public int size() {
        return size;
    }

    // Add before a given existing value

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addBefore(T existing, T newData) {
        if (head == null) return false;

        Node<T> current = head;

        while (current != null) {
            if (current.data.equals(existing)) {

                Node<T> newNode = new Node<>(newData);

                newNode.next = current;
                newNode.prev = current.prev;

                if (current.prev != null) {
                    current.prev.next = newNode;
                } else {
                    head = newNode; // inserting before head
                }

                current.prev = newNode;

                size++;
                return true;
            }

            current = current.next;
        }

        return false; // existing not found
    }

    // Add after a given existing value

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAfter(T existing, T newData) {
        if (head == null) return false;

        Node<T> current = head;

        while (current != null) {
            if (current.data.equals(existing)) {

                Node<T> newNode = new Node<>(newData);

                newNode.prev = current;
                newNode.next = current.next;

                if (current.next != null) {
                    current.next.prev = newNode;
                } else {
                    tail = newNode; // inserting after tail
                }

                current.next = newNode;

                size++;
                return true;
            }

            current = current.next;
        }

        return false; // existing not found
    }
}
