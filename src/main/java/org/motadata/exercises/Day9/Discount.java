package org.motadata.exercises.Day9;

/**
 * A record representing a discount that can be applied to a total price.
 *
 * @param code       the unique coupon code
 * @param percentage the discount percentage (0-100)
 * @author prit.thakkar@motadata.com
 */
public record Discount(String code, double percentage) {
    /**
     * Applies the discount to the given total.
     *
     * @param total original total price
     * @return discounted price
     */
    public double apply(double total) {
        return total - (total * percentage / 100.0);
    }

    @Override
    public String toString() {
        return "Discount{" +
                "code='" + code + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
