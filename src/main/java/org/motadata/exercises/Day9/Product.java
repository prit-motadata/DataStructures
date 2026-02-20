package org.motadata.exercises.Day9;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a product in the ecommerce system with concurrent stock
 * management.
 *
 * @author prit.thakkar@motadata.com
 */
public class Product {

    private final String id;
    private final String name;
    private final double price;

    private final AtomicInteger availableQuantity;

    /**
     * Creates a new product.
     *
     * @param id                unique identifier
     * @param name              product name
     * @param price             unit price
     * @param availableQuantity initial stock quantity
     */
    public Product(String id, String name, double price, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableQuantity = new AtomicInteger(availableQuantity);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity.get();
    }

    /**
     * Atomically reduces product stock if enough is available.
     *
     * @param quantity amount to reduce
     * @throws IllegalArgumentException if quantity is greater than available stock
     */
    public void reduceStock(int quantity) {
        while (true) {
            int current = availableQuantity.get();
            if (quantity > current) {
                throw new IllegalArgumentException("Not enough stock");
            }
            if (availableQuantity.compareAndSet(current, current - quantity)) {
                return;
            }
        }
    }

    /**
     * Atomically increases product stock.
     *
     * @param quantity amount to increase
     */
    public void increaseStock(int quantity) {
        availableQuantity.addAndGet(quantity);
    }
}
