package org.motadata.datastructures.linkedlist;

public interface BidirectionalList<T> extends List<T> {
    boolean addBefore(T existing, T newData);
    boolean addAfter(T existing, T newData);
    void displayReverse();
}
