package org.motadata.exercises.Day8;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory library catalog that indexes {@link Book} instances by both ISBN and title.
 *
 * <p>Uses a primary index on ISBN and a secondary index on lowercased book titles
 * to support fast lookup by either field.</p>
 *
 * @author prit.thakkar@motadata.com
 */
public class LibraryCatalog {

    // Primary index
    private final Map<String, Book> booksByIsbn = new HashMap<>();

    // Secondary index
    private final Map<String, Book> booksByTitle = new HashMap<>();

    /**
     * Adds a new book to the catalog if its ISBN does not already exist.
     *
     * @param book book to add
     * @return {@code true} if the book was added, {@code false} if it was invalid or a duplicate
     * @see #removeBook(String)
     * @see #searchByIsbn(String)
     * @see #searchByTitle(String)
     */
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

    /**
     * Removes a book with the given ISBN from the catalog.
     *
     * @param isbn ISBN of the book to remove
     * @return {@code true} if a book was removed, {@code false} otherwise
     * @see #addBook(Book)
     */
    public boolean removeBook(String isbn) {
        Book removed = booksByIsbn.remove(isbn);
        if (removed == null) {
            return false;
        }

        booksByTitle.remove(removed.title().toLowerCase());
        return true;
    }

    /**
     * Looks up a book by its ISBN.
     *
     * @param isbn ISBN to search for
     * @return matching {@link Book} or {@code null} if none exists
     */
    public Book searchByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }

    /**
     * Looks up a book by its title (case-insensitive).
     *
     * @param title title of the book to search
     * @return matching {@link Book} or {@code null} if none exists
     */
    public Book searchByTitle(String title) {
        if (title == null) return null;
        return booksByTitle.get(title.toLowerCase());
    }

    /**
     * Returns the number of books currently stored in the catalog.
     *
     * @return total book count
     */
    public int totalBooks() {
        return booksByIsbn.size();
    }
}
