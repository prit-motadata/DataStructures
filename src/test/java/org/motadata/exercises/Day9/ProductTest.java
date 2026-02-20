package org.motadata.exercises.Day9;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testInitialValues() {
        Product p = new Product("1", "Laptop", 1000, 10);

        assertEquals("1", p.getId());
        assertEquals("Laptop", p.getName());
        assertEquals(1000, p.getPrice());
        assertEquals(10, p.getAvailableQuantity());
    }

    @Test
    void testReduceStock() {
        Product p = new Product("1", "Phone", 500, 10);
        p.reduceStock(3);

        assertEquals(7, p.getAvailableQuantity());
    }

    @Test
    void testIncreaseStock() {
        Product p = new Product("1", "Phone", 500, 5);
        p.increaseStock(5);

        assertEquals(10, p.getAvailableQuantity());
    }

    @Test
    void testReduceStockException() {
        Product p = new Product("1", "Phone", 500, 5);

        assertThrows(IllegalArgumentException.class, () -> p.reduceStock(10));
    }

    @Test
    void testStockOperations() {
        Product p = new Product("1", "Phone", 500, 10);

        assertEquals(10, p.getAvailableQuantity());

        p.reduceStock(5);
        assertEquals(5, p.getAvailableQuantity());

        p.increaseStock(3);
        assertEquals(8, p.getAvailableQuantity());
    }
}