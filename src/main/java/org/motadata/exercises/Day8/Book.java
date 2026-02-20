package org.motadata.exercises.Day8;

import java.util.Objects;

/**
 * Immutable representation of a book identified by ISBN, title, and author.
 *
 * @param isbn   unique ISBN identifier for the book
 * @param title  human-readable title
 * @param author name of the book's author
 * @author prit.thakkar@motadata.com
 */
public record Book(String isbn, String title, String author) {

    /**
     * Constructs a new {@code Book}, ensuring that all fields are non-null.
     *
     * @param isbn   unique ISBN identifier for the book
     * @param title  human-readable title
     * @param author name of the book's author
     * @throws NullPointerException if any argument is {@code null}
     */
    public Book(String isbn, String title, String author) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    /**
     * Returns a string representation useful for logging and debugging.
     *
     * @return string representation of this book
     */
    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
