package org.motadata.exercises.Day9;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe shopping cart implementation using concurrent data structures.
 *
 * <p>
 * Supports managing cart items, applying discounts, and maintaining a wishlist.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class ShoppingCart {

    private final ConcurrentHashMap<String, CartItem> items = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Discount> discounts = new ConcurrentHashMap<>();
    private final Set<String> wishlist = ConcurrentHashMap.newKeySet();
    private final AtomicReference<Double> total = new AtomicReference<>(0.0);

    // ---------------- CART ----------------

    /**
     * Adds a product to the cart or increases quantity if it already exists.
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     */
    public void addItem(Product product, int quantity) {
        items.compute(product.getId(), (id, existing) -> {
            if (existing == null) {
                total.updateAndGet(t -> t + product.getPrice() * quantity);
                return new CartItem(product, quantity);
            } else {
                existing.increaseQuantity(quantity);
                total.updateAndGet(t -> t + product.getPrice() * quantity);
                return existing;
            }
        });
    }

    /**
     * Removes a product entirely from the cart.
     *
     * @param productId id of the product to remove
     */
    public void removeItem(String productId) {
        CartItem removed = items.remove(productId);
        if (removed != null) {
            total.updateAndGet(t -> t - removed.totalPrice());
        }
    }

    /**
     * Updates the quantity of a specific product in the cart.
     *
     * @param productId   id of the product
     * @param newQuantity the new desired quantity
     */
    public void updateQuantity(String productId, int newQuantity) {
        items.computeIfPresent(productId, (id, item) -> {

            int oldQty = item.quantity();
            double price = item.product().getPrice();

            if (newQuantity <= 0) {
                total.updateAndGet(t -> t - (oldQty * price));
                return null;
            } else {
                int diff = newQuantity - oldQty;
                item.setQuantity(newQuantity);
                total.updateAndGet(t -> t + (diff * price));
                return item;
            }
        });
    }

    /**
     * Returns the total price of all items in the cart.
     *
     * @return current total price
     */
    public double getTotal() {
        return total.get();
    }

    // ---------------- DISCOUNTS ----------------

    /**
     * Registers a discount code.
     *
     * @param discount the discount to add
     */
    public void addDiscount(Discount discount) {
        discounts.put(discount.code(), discount);
    }

    /**
     * Calculates the total price after applying a specific discount code.
     *
     * @param code the coupon code
     * @return the discounted total price
     */
    public double applyDiscount(String code) {
        Discount discount = discounts.get(code);
        if (discount == null) {
            return total.get();
        }
        return discount.apply(total.get());
    }

    // ---------------- WISHLIST ----------------

    /**
     * Adds a product to the wishlist.
     *
     * @param productId id of the product
     */
    public void addToWishlist(String productId) {
        wishlist.add(productId);
    }

    /**
     * Removes a product from the wishlist.
     *
     * @param productId id of the product
     */
    public void removeFromWishlist(String productId) {
        wishlist.remove(productId);
    }

    /**
     * Checks if a product is in the wishlist.
     *
     * @param productId id of the product
     * @return {@code true} if in wishlist, {@code false} otherwise
     */
    public boolean isInWishlist(String productId) {
        return wishlist.contains(productId);
    }

    /**
     * Returns the number of distinct items in the cart.
     *
     * @return item count
     */
    public int totalItems() {
        return items.size();
    }
}
