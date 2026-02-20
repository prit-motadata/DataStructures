package org.motadata.exercises.Day10;

import org.motadata.common.factory.map.MapFactory;
import org.motadata.common.factory.map.MapType;
import org.motadata.datastructures.hashmap.Map;

import java.util.Objects;

/**
 * A service for shortening URLs and resolving them back to their original form.
 *
 * <p>
 * Uses a customizable {@link Map} implementation for storing the mappings.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class UrlShortenerService {

    private static final String BASE_URL = "https://short.ly/";

    // shortCode -> originalUrl
    private final Map<String, String> urlStore;

    private final UrlGenerator generator = new UrlGenerator();

    /**
     * Creates a new service with the specified map implementation.
     *
     * @param type the type of map to use for storage
     */
    public UrlShortenerService(MapType type) {
        this.urlStore = MapFactory.createMap(type);
    }

    /**
     * Creates a new service with the default map implementation.
     */
    public UrlShortenerService() {
        this.urlStore = MapFactory.createDefault();
    }

    /**
     * Shortens the given URL and stores the mapping.
     *
     * @param originalUrl the URL to shorten
     * @return the shortened URL with the base prefix
     * @throws NullPointerException if originalUrl is null
     */
    public String shortenUrl(String originalUrl) {
        Objects.requireNonNull(originalUrl, "URL cannot be null");

        String shortCode = generator.generate();

        urlStore.put(shortCode, originalUrl);

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
