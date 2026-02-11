package org.motadata.exercises.Day9;

import org.motadata.datastructures.hashmap.GenericBucketHashMap;
import org.motadata.datastructures.hashmap.Map;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {

    private final Map<String, CartItem> items = new GenericBucketHashMap<>();
    private final Map<String, Discount> discounts = new GenericBucketHashMap<>();
    private final Set<String> wishlist = new HashSet<>();

    // ---------------- CART ----------------

    public void addItem(Product product, int quantity) {
        CartItem item = items.get(product.id());

        if (item == null) {
            items.put(product.id(), new CartItem(product, quantity));
        } else {
            item.increaseQuantity(quantity);
        }
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateQuantity(String productId, int quantity) {
        CartItem item = items.get(productId);
        if (item == null) return;

        if (quantity <= 0) {
            items.remove(productId);
        } else {
            item.decreaseQuantity(item.quantity() - quantity);
        }
    }

    public double calculateTotal() {
        return items.values()
                .stream()
                .mapToDouble(CartItem::totalPrice)
                .sum();
    }

    // ---------------- DISCOUNTS ----------------

    public void addDiscount(Discount discount) {
        discounts.put(discount.code(), discount);
    }

    public double applyDiscount(String code) {
        Discount discount = discounts.get(code);
        if (discount == null) {
            return calculateTotal();
        }
        return discount.apply(calculateTotal());
    }

    // ---------------- WISHLIST ----------------

    public void addToWishlist(String productId) {
        wishlist.add(productId);
    }

    public void removeFromWishlist(String productId) {
        wishlist.remove(productId);
    }

    public boolean isInWishlist(String productId) {
        return wishlist.contains(productId);
    }

    public int totalItems() {
        return items.size();
    }
}
