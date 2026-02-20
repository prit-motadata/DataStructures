package org.motadata.exercises.Day10;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UrlShortenerServiceConcurrent {
    private static final String BASE_URL = "https://short.ly/";

    // Thread-safe map
    private final ConcurrentMap<String, String> urlStore =
            new ConcurrentHashMap<>();

    private final UrlGenerator generator = new UrlGenerator();

    public String shortenUrl(String originalUrl) {
        Objects.requireNonNull(originalUrl, "URL cannot be null");

        String shortCode;

        // Collision-safe under concurrency
        do {
            shortCode = generator.generate();
        } while (urlStore.putIfAbsent(shortCode, originalUrl) != null);

        return BASE_URL + shortCode;
    }

    public String getOriginalUrl(String shortUrl) {
        if (shortUrl == null || !shortUrl.startsWith(BASE_URL)) {
            return null;
        }

        String shortCode = shortUrl.substring(BASE_URL.length());
        return urlStore.get(shortCode);
    }

    public int totalUrls() {
        return urlStore.size();
    }
}
