package org.motadata.datastructures.linkedlist;

import java.util.function.Predicate;

public interface List<T> {
    void addFirst(T data);
    void addLast(T data);
    boolean remove(T data);
    boolean contains(T data);
    boolean isEmpty();
    List<T> search(Predicate<T> condition);
    void display();
}
