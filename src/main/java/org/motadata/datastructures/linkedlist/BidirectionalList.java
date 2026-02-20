package org.motadata.datastructures.linkedlist;

/**
 * Extension of {@link List} that supports bidirectional traversal and insertion relative to existing elements.
 *
 * @param <T> element type stored in the list
 * @author prit.thakkar@motadata.com
 */
public interface BidirectionalList<T> extends List<T> {

    /**
     * Inserts a new element immediately before the first occurrence of an existing element.
     *
     * @param existing element before which the new element should be inserted
     * @param newData  element to insert
     * @return {@code true} if the existing element was found and insertion succeeded, {@code false} otherwise
     */
    boolean addBefore(T existing, T newData);

    /**
     * Inserts a new element immediately after the first occurrence of an existing element.
     *
     * @param existing element after which the new element should be inserted
     * @param newData  element to insert
     * @return {@code true} if the existing element was found and insertion succeeded, {@code false} otherwise
     */
    boolean addAfter(T existing, T newData);

    /**
     * Prints the list elements in reverse order to standard output.
     */
    void displayReverse();
}
