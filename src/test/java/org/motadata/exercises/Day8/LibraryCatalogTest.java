package org.motadata.exercises.Day8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryCatalogTest {

    private LibraryCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new LibraryCatalog();
    }

    // -------- ADD BOOK --------

    @Test
    void addBook_success() {
        Book book = new Book("111", "Java Basics", "Author A");

        boolean added = catalog.addBook(book);

        assertTrue(added);
        assertEquals(1, catalog.totalBooks());
    }

    @Test
    void addBook_duplicateIsbn_returnsFalse() {
        Book book1 = new Book("111", "Java Basics", "Author A");
        Book book2 = new Book("111", "Advanced Java", "Author B");

        catalog.addBook(book1);
        boolean result = catalog.addBook(book2);

        assertFalse(result);
        assertEquals(1, catalog.totalBooks());
    }

    @Test
    void addBook_nullBook_returnsFalse() {
        assertFalse(catalog.addBook(null));
    }

    @Test
    void bookConstructor_nullIsbn_throwsException() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new Book(null, "Title", "Author")
        );

        assertEquals("ISBN cannot be null", ex.getMessage());
    }

    @Test
    void bookConstructor_nullTitle_throwsException() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new Book("123", null, "Author")
        );

        assertEquals("Title cannot be null", ex.getMessage());
    }

    @Test
    void bookConstructor_nullAuthor_throwsException() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new Book("123", "Title", null)
        );

        assertEquals("Author cannot be null", ex.getMessage());
    }

    // -------- REMOVE BOOK --------

    @Test
    void removeBook_success() {
        Book book = new Book("222", "Spring Boot", "Author B");
        catalog.addBook(book);

        boolean removed = catalog.removeBook("222");

        assertTrue(removed);
        assertEquals(0, catalog.totalBooks());
        assertNull(catalog.searchByTitle("Spring Boot"));
    }

    @Test
    void removeBook_nonExisting_returnsFalse() {
        assertFalse(catalog.removeBook("999"));
    }

    // -------- SEARCH BY ISBN --------

    @Test
    void searchByIsbn_found() {
        Book book = new Book("333", "Hibernate", "Author C");
        catalog.addBook(book);

        Book result = catalog.searchByIsbn("333");

        assertNotNull(result);
        assertEquals("Hibernate", result.title());
    }

    @Test
    void searchByIsbn_notFound_returnsNull() {
        assertNull(catalog.searchByIsbn("000"));
    }

    // -------- SEARCH BY TITLE --------

    @Test
    void searchByTitle_found_caseInsensitive() {
        Book book = new Book("444", "Microservices", "Author D");
        catalog.addBook(book);

        Book result = catalog.searchByTitle("microservices");

        assertNotNull(result);
        assertEquals("444", result.isbn());
    }

    @Test
    void searchByTitle_null_returnsNull() {
        assertNull(catalog.searchByTitle(null));
    }

    @Test
    void searchByTitle_notFound_returnsNull() {
        assertNull(catalog.searchByTitle("Unknown Book"));
    }

    // -------- TOTAL BOOKS --------

    @Test
    void totalBooks_correctCount() {
        catalog.addBook(new Book("1", "A", "X"));
        catalog.addBook(new Book("2", "B", "Y"));

        assertEquals(2, catalog.totalBooks());

        catalog.removeBook("1");

        assertEquals(1, catalog.totalBooks());
    }
}
