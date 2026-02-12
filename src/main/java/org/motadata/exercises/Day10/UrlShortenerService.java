package org.motadata.exercises.Day10;

import org.motadata.common.factory.MapFactory;
import org.motadata.common.factory.MapType;
import org.motadata.datastructures.hashmap.Map;

import java.util.Objects;

public class UrlShortenerService {

    private static final String BASE_URL = "https://short.ly/";

    // shortCode -> originalUrl
    private final Map<String, String> urlStore;

    private final UrlGenerator generator = new UrlGenerator();

    public UrlShortenerService(MapType type) {
        this.urlStore = MapFactory.createMap(type);
    }

    // Default constructor
    public UrlShortenerService() {
        this.urlStore = MapFactory.createDefault();
    }

    // ---------------- ADD NEW URL ----------------

    public String shortenUrl(String originalUrl) {
        Objects.requireNonNull(originalUrl, "URL cannot be null");

        String shortCode = generator.generate();

        urlStore.put(shortCode, originalUrl);

        return BASE_URL + shortCode;
    }

    // ---------------- RETRIEVE ORIGINAL ----------------

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
