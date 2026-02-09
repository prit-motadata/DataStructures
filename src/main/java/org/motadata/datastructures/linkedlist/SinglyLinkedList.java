package org.motadata.datastructures.linkedlist;

public class SinglyLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;

    // Add at beginning
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

    public boolean isEmpty() {
        return head == null;
    }
}
