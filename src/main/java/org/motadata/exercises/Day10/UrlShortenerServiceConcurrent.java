package org.motadata.exercises.Day10;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread-safe URL shortening service suitable for high-concurrency
 * environments.
 *
 * <p>
 * Uses {@link ConcurrentHashMap} for storage and handles potential short code
 * collisions atomically.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class UrlShortenerServiceConcurrent {
    private static final String BASE_URL = "https://short.ly/";

    // Thread-safe map
    private final ConcurrentMap<String, String> urlStore = new ConcurrentHashMap<>();

    private final UrlGenerator generator = new UrlGenerator();

    /**
     * Shortens the given URL in a thread-safe manner.
     *
     * @param originalUrl the URL to shorten
     * @return the shortened URL with the base prefix
     * @throws NullPointerException if originalUrl is null
     */
    public String shortenUrl(String originalUrl) {
        Objects.requireNonNull(originalUrl, "URL cannot be null");

        String shortCode;

        // Collision-safe under concurrency
        do {
            shortCode = generator.generate();
        } while (urlStore.putIfAbsent(shortCode, originalUrl) != null);

        return BASE_URL + shortCode;
    }

    /**
     * Retrieves the original URL associated with the given shortened URL.
     *
     * @param shortUrl the full shortened URL
     * @return the original URL, or {@code null} if not found or invalid format
     */
    public String getOriginalUrl(String shortUrl) {
        if (shortUrl == null || !shortUrl.startsWith(BASE_URL)) {
            return null;
        }

        String shortCode = shortUrl.substring(BASE_URL.length());
        return urlStore.get(shortCode);
    }

    /**
     * Returns the total number of shortened URLs currently stored.
     *
     * @return mapping count
     */
    public int totalUrls() {
        return urlStore.size();
    }
}
