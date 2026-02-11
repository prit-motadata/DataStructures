package org.motadata.exercises.Day8;

import java.util.HashMap;
import java.util.Map;

public class LibraryCatalog {

    // Primary index
    private final Map<String, Book> booksByIsbn = new HashMap<>();

    // Secondary index
    private final Map<String, Book> booksByTitle = new HashMap<>();

    // Add book
    public boolean addBook(Book book) {
        if (book == null || book.isbn() == null || book.title() == null) {
            return false;
        }

        if (booksByIsbn.containsKey(book.isbn())) {
            return false; // Duplicate ISBN not allowed
        }

        booksByIsbn.put(book.isbn(), book);
        booksByTitle.put(book.title().toLowerCase(), book);

        return true;
    }

    // Remove book by ISBN
    public boolean removeBook(String isbn) {
        Book removed = booksByIsbn.remove(isbn);
        if (removed == null) {
            return false;
        }

        booksByTitle.remove(removed.title().toLowerCase());
        return true;
    }

    // Search book by ISBN
    public Book searchByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }

    // Search book by Title
    public Book searchByTitle(String title) {
        if (title == null) return null;
        return booksByTitle.get(title.toLowerCase());
    }

    public int totalBooks() {
        return booksByIsbn.size();
    }
}
