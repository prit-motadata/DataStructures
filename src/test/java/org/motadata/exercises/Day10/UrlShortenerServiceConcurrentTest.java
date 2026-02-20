package org.motadata.exercises.Day10;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerServiceConcurrentTest {

    @Test
    void testShortenUrlSuccess() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        String shortUrl = service.shortenUrl("https://google.com");

        assertNotNull(shortUrl);
        assertTrue(shortUrl.startsWith("https://short.ly/"));
        assertEquals(1, service.totalUrls());
    }

    @Test
    void testShortenUrlNullThrowsException() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        assertThrows(NullPointerException.class,
                () -> service.shortenUrl(null));
    }

    @Test
    void testGetOriginalUrlSuccess() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        String shortUrl = service.shortenUrl("https://example.com");
        String original = service.getOriginalUrl(shortUrl);

        assertEquals("https://example.com", original);
    }

    @Test
    void testGetOriginalUrlNullInput() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        assertNull(service.getOriginalUrl(null));
    }

    @Test
    void testGetOriginalUrlInvalidPrefix() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        assertNull(service.getOriginalUrl("https://invalid.com/abc"));
    }

    @Test
    void testGetOriginalUrlValidFormatButNotStored() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        assertNull(service.getOriginalUrl("https://short.ly/unknown"));
    }

    @Test
    void testTotalUrlsAfterMultipleInsertions() {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        service.shortenUrl("https://a.com");
        service.shortenUrl("https://b.com");
        service.shortenUrl("https://c.com");

        assertEquals(3, service.totalUrls());
    }

    @Test
    void testConcurrentShortening() throws Exception {
        UrlShortenerServiceConcurrent service =
                new UrlShortenerServiceConcurrent();

        int threads = 50;
        List<Future<String>> results;
        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {

            List<Callable<String>> tasks = new ArrayList<>();

            for (int i = 0; i < threads; i++) {
                final int index = i;
                tasks.add(() ->
                        service.shortenUrl("https://site" + index + ".com"));
            }

            results = executor.invokeAll(tasks);

            executor.shutdown();
        }

        assertEquals(threads, service.totalUrls());

        for (Future<String> future : results) {
            String shortUrl = future.get();
            assertTrue(shortUrl.startsWith("https://short.ly/"));
            assertNotNull(service.getOriginalUrl(shortUrl));
        }
    }
}