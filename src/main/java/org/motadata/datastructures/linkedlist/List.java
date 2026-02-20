package org.motadata.datastructures.linkedlist;

import java.util.function.Predicate;

/**
 * Basic list abstraction supporting insertion at both ends, removal, search, and display.
 *
 * @param <T> element type stored in the list
 * @author prit.thakkar@motadata.com
 */
public interface List<T> {

    /**
     * Inserts the given element at the beginning of the list.
     *
     * @param data element to add at the front
     */
    void addFirst(T data);

    /**
     * Appends the given element to the end of the list.
     *
     * @param data element to add at the end
     */
    void addLast(T data);

    /**
     * Removes the first occurrence of the given element from the list, if present.
     *
     * @param data element to remove
     * @return {@code true} if an element was removed, {@code false} otherwise
     */
    boolean remove(T data);

    /**
     * Returns whether the list contains the given element.
     *
     * @param data element to search for
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    boolean contains(T data);

    /**
     * Returns whether this list has no elements.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns a list containing all elements that satisfy the given predicate.
     *
     * @param condition predicate used to filter elements
     * @return new list with all matching elements
     */
    List<T> search(Predicate<T> condition);

    /**
     * Prints a human-readable representation of the list to standard output.
     */
    void display();
}
