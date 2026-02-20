package org.motadata.common.factory.list;

import org.motadata.datastructures.linkedlist.DoublyLinkedList;
import org.motadata.datastructures.linkedlist.SinglyLinkedList;
import org.motadata.datastructures.linkedlist.List;

/**
 * Factory class for creating different types of List implementations.
 *
 * @author prit.thakkar@motadata.com
 */
public final class ListFactory {
    private ListFactory() {}

    /**
     * Creates a list of the specified type.
     *
     * @param <T>  the type of elements in the list
     * @param type the type of list implementation to create
     * @return a new List instance
     */
    public static <T> List<T> createList(ListType type) {
        return switch (type) {
            case DOUBLY -> new DoublyLinkedList<>();
            case SINGLY -> new SinglyLinkedList<>();
        };
    }

    /**
     * Creates a default list implementation (SinglyLinkedList).
     *
     * @param <T> the type of elements in the list
     * @return a new default List instance
     */
    public static <T> List<T> createDefault() {
        return new SinglyLinkedList<>();
    }
}
