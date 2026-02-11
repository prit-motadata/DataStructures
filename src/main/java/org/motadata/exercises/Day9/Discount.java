package org.motadata.exercises.Day9;

public record Discount(String code, double percentage) {
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
