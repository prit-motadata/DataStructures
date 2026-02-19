package org.motadata.common.factory.list;

import org.motadata.datastructures.linkedlist.DoublyLinkedList;
import org.motadata.datastructures.linkedlist.SinglyLinkedList;
import org.motadata.datastructures.linkedlist.List;

public final class ListFactory {
    private ListFactory() {}

    public static <T> List<T> createList(ListType type) {
        return switch (type) {
            case DOUBLY -> new DoublyLinkedList<>();
            case SINGLY -> new SinglyLinkedList<>();
        };
    }

    public static <T> List<T> createDefault() {
        return new SinglyLinkedList<>();
    }
}
