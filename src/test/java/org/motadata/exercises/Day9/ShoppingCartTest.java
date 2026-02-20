package org.motadata.exercises.Day9;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;
    private Product product;

    @BeforeEach
    void setup() {
        cart = new ShoppingCart();
        product = new Product("p1", "Keyboard", 100, 100);
    }

    @Test
    void testAddAndTotal() {
        cart.addItem(product, 2);
        assertEquals(200, cart.getTotal());
        assertEquals(1, cart.totalItems());
    }

    @Test
    void testRemoveItem() {
        cart.addItem(product, 2);
        cart.removeItem(product.getId());

        assertEquals(0, cart.getTotal());
        assertEquals(0, cart.totalItems());
    }

    @Test
    void testUpdateQuantityIncrease() {
        cart.addItem(product, 1);
        cart.updateQuantity("p1", 5);

        assertEquals(500, cart.getTotal());
    }

    @Test
    void testUpdateQuantityDecrease() {
        cart.addItem(product, 5);
        cart.updateQuantity("p1", 2);

        assertEquals(200, cart.getTotal());
    }

    @Test
    void testConstructorValid() {
        Product product = new Product("p1", "Mouse", 50, 100);
        CartItem item = new CartItem(product, 5);

        assertEquals(5, item.quantity());
        assertEquals(product, item.product());
    }

    @Test
    void testConstructorInvalidQuantity() {
        Product product = new Product("p1", "Mouse", 50, 100);

        assertThrows(IllegalArgumentException.class,
                () -> new CartItem(product, 0));

        assertThrows(IllegalArgumentException.class,
                () -> new CartItem(product, -5));
    }

    @Test
    void testIncreaseQuantity() {
        Product product = new Product("p1", "Mouse", 50, 100);
        CartItem item = new CartItem(product, 2);

        item.increaseQuantity(3);

        assertEquals(5, item.quantity());
    }

    @Test
    void testDecreaseQuantityNormal() {
        Product product = new Product("p1", "Mouse", 50, 100);
        CartItem item = new CartItem(product, 5);

        item.decreaseQuantity(2);

        assertEquals(3, item.quantity());
    }

    @Test
    void testDecreaseQuantityToZero() {
        Product product = new Product("p1", "Mouse", 50, 100);
        CartItem item = new CartItem(product, 5);

        item.decreaseQuantity(10); // goes below zero branch

        assertEquals(0, item.quantity());
    }

    @Test
    void testUpdateQuantityZero() {
        cart.addItem(product, 3);
        cart.updateQuantity("p1", 0);

        assertEquals(0, cart.getTotal());
    }

    @Test
    void testUpdateNonExisting() {
        cart.updateQuantity("unknown", 5);
        assertEquals(0, cart.getTotal());
    }

    @Test
    void testApplyDiscountValid() {
        cart.addItem(product, 2);

        Discount discount = new Discount("SAVE10", 10);
        cart.addDiscount(discount);

        assertEquals(180, cart.applyDiscount("SAVE10"));
    }

    @Test
    void testApplyDiscountInvalid() {
        cart.addItem(product, 2);

        assertEquals(200, cart.applyDiscount("INVALID"));
    }

    @Test
    void testWishlist() {
        cart.addToWishlist("p1");
        assertTrue(cart.isInWishlist("p1"));

        cart.removeFromWishlist("p1");
        assertFalse(cart.isInWishlist("p1"));
    }

    @Test
    void testConcurrentAdd() throws Exception {
        int threads = 10;

        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {

            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < threads; i++) {
                tasks.add(() -> {
                    cart.addItem(product, 1);
                    return null;
                });
            }

            executor.invokeAll(tasks);  // waits for completion
            executor.shutdown();
        }

        assertEquals(1000, cart.getTotal());
    }
}
