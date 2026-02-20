package org.motadata.exercises.Day9;

public class CartItem {

    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        quantity -= amount;
        if (quantity <= 0) {
            quantity = 0;
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double totalPrice() {
        return product.getPrice() * quantity;
    }

    public int quantity() {
        return quantity;
    }

    public Product product() {
        return product;
    }
}
