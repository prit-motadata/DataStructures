package org.motadata.exercises.Day9;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ShoppingCart {

    private final ConcurrentHashMap<String, CartItem> items = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Discount> discounts = new ConcurrentHashMap<>();
    private final Set<String> wishlist = ConcurrentHashMap.newKeySet();
    private final AtomicReference<Double> total = new AtomicReference<>(0.0);

    // ---------------- CART ----------------

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

    public void removeItem(String productId) {
        CartItem removed = items.remove(productId);
        if (removed != null) {
            total.updateAndGet(t -> t - removed.totalPrice());
        }
    }

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

    public double getTotal() {
        return total.get();
    }

    // ---------------- DISCOUNTS ----------------

    public void addDiscount(Discount discount) {
        discounts.put(discount.code(), discount);
    }

    public double applyDiscount(String code) {
        Discount discount = discounts.get(code);
        if (discount == null) {
            return total.get();
        }
        return discount.apply(total.get());
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
