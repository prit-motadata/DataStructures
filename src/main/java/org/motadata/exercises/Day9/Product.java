package org.motadata.exercises.Day9;

import java.util.concurrent.atomic.AtomicInteger;

public class Product {

    private final String id;
    private final String name;
    private final double price;

    private final AtomicInteger availableQuantity;

    public Product(String id, String name, double price, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableQuantity = new AtomicInteger(availableQuantity);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public int getAvailableQuantity() {
        return availableQuantity.get();
    }

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

    public void increaseStock(int quantity) {
        availableQuantity.addAndGet(quantity);
    }
}
