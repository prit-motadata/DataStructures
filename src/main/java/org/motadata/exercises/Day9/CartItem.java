package org.motadata.exercises.Day9;

/**
 * Represents an item in the shopping cart, linking a product with its selected
 * quantity.
 *
 * @author prit.thakkar@motadata.com
 */
public class CartItem {

    private final Product product;
    private int quantity;

    /**
     * Creates a new cart item.
     *
     * @param product  the product being added
     * @param quantity the initial quantity
     * @throws IllegalArgumentException if quantity is 0 or less
     */
    public CartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Increases the quantity of the item in the cart.
     *
     * @param amount the quantity to increase by
     */
    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    /**
     * Decreases the quantity of the item in the cart.
     *
     * @param amount the quantity to decrease by
     */
    public void decreaseQuantity(int amount) {
        quantity -= amount;
        if (quantity <= 0) {
            quantity = 0;
        }
    }

    /**
     * Explicitly sets the quantity of the item in the cart.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the total price for this item (price * quantity).
     *
     * @return total price
     */
    public double totalPrice() {
        return product.getPrice() * quantity;
    }

    /**
     * Returns the current quantity of the item.
     *
     * @return current quantity
     */
    public int quantity() {
        return quantity;
    }

    /**
     * Returns the product associated with this cart item.
     *
     * @return the product
     */
    public Product product() {
        return product;
    }
}
