package org.motadata.exercises.Day10;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility for generating unique short codes using Base62 encoding.
 *
 * <p>
 * Uses an {@link AtomicLong} counter to ensure uniqueness across calls.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class UrlGenerator {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = 62;

    private final AtomicLong counter = new AtomicLong(1);

    /**
     * Generates a new unique short code.
     *
     * @return a unique Base62 encoded string
     */
    public String generate() {
        long value = counter.getAndIncrement();
        return encodeBase62(value);
    }

    /**
     * Encodes a long value into a Base62 string.
     *
     * @param value the number to encode
     * @return Base62 representation
     */
    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }

        return sb.reverse().toString();
    }
}
