package org.motadata.exercises.Day10;

import java.util.concurrent.atomic.AtomicLong;

public class UrlGenerator {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = 62;

    private final AtomicLong counter = new AtomicLong(1);

    public String generate() {
        long value = counter.getAndIncrement();
        return encodeBase62(value);
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }

        return sb.reverse().toString();
    }
}
