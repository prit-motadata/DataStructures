package org.motadata.exercises.Day9;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;
    private Product p1;
    private Product p2;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        p1 = new Product("1", "Laptop", 1000.0);
        p2 = new Product("2", "Mouse", 100.0);
    }

    // ---------------- CART ----------------

    @Test
    void addNewItem_shouldIncreaseSize() {
        cart.addItem(p1, 2);

        assertEquals(1, cart.totalItems());
        assertEquals(2000.0, cart.calculateTotal());
    }

    @Test
    void addExistingItem_shouldIncreaseQuantity() {
        cart.addItem(p1, 1);
        cart.addItem(p1, 2);

        assertEquals(1, cart.totalItems());
        assertEquals(3000.0, cart.calculateTotal());
    }

    @Test
    void removeItem_shouldRemoveFromCart() {
        cart.addItem(p1, 1);
        cart.removeItem("1");

        assertEquals(0, cart.totalItems());
        assertEquals(0.0, cart.calculateTotal());
    }

    @Test
    void updateQuantity_shouldUpdateCorrectly() {
        cart.addItem(p1, 5);
        cart.updateQuantity("1", 2);

        assertEquals(2000.0, cart.calculateTotal());
    }

    @Test
    void updateQuantity_zeroOrLess_shouldRemoveItem() {
        cart.addItem(p1, 3);
        cart.updateQuantity("1", 0);

        assertEquals(0, cart.totalItems());
    }

    @Test
    void updateQuantity_nonExistingItem_shouldDoNothing() {
        cart.updateQuantity("unknown", 5);

        assertEquals(0, cart.totalItems());
    }

    @Test
    void calculateTotal_multipleItems() {
        cart.addItem(p1, 1);
        cart.addItem(p2, 2);

        assertEquals(1200.0, cart.calculateTotal());
    }

    // ---------------- DISCOUNTS ----------------

    @Test
    void applyDiscount_existingCode() {
        cart.addItem(p1, 1);

        Discount discount = new Discount("SALE10", 10);
        cart.addDiscount(discount);

        double total = cart.applyDiscount("SALE10");

        assertEquals(900.0, total);
    }

    @Test
    void applyDiscount_nonExistingCode_shouldReturnOriginalTotal() {
        cart.addItem(p1, 1);

        double total = cart.applyDiscount("INVALID");

        assertEquals(1000.0, total);
    }

    // ---------------- WISHLIST ----------------

    @Test
    void wishlist_addAndCheck() {
        cart.addToWishlist("1");

        assertTrue(cart.isInWishlist("1"));
    }

    @Test
    void wishlist_remove() {
        cart.addToWishlist("1");
        cart.removeFromWishlist("1");

        assertFalse(cart.isInWishlist("1"));
    }

    @Test
    void wishlist_nonExistingItem() {
        assertFalse(cart.isInWishlist("999"));
    }

    // ---------------- EDGE CASES ----------------

    @Test
    void calculateTotal_emptyCart_shouldBeZero() {
        assertEquals(0.0, cart.calculateTotal());
    }

    @Test
    void applyDiscount_emptyCart_shouldBeZero() {
        Discount discount = new Discount("SALE10", 10);
        cart.addDiscount(discount);

        assertEquals(0.0, cart.applyDiscount("SALE10"));
    }
}
