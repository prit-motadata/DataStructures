package org.motadata.exercises.Day10;

import org.junit.jupiter.api.Test;
import org.motadata.common.factory.map.MapType;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerServiceTest {
    @Test
    void testDefaultConstructor() {
        UrlShortenerService service = new UrlShortenerService();
        assertEquals(0, service.totalUrls());
    }

    @Test
    void testConstructorWithMapType() {
        UrlShortenerService service =
                new UrlShortenerService(MapType.GENERIC);

        assertEquals(0, service.totalUrls());
    }

    @Test
    void testShortenUrlSuccess() {
        UrlShortenerService service = new UrlShortenerService();

        String shortUrl = service.shortenUrl("https://google.com");

        assertNotNull(shortUrl);
        assertTrue(shortUrl.startsWith("https://short.ly/"));
        assertEquals(1, service.totalUrls());
    }

    @Test
    void testShortenUrlNullThrowsException() {
        UrlShortenerService service = new UrlShortenerService();

        assertThrows(NullPointerException.class,
                () -> service.shortenUrl(null));
    }

    @Test
    void testGetOriginalUrlSuccess() {
        UrlShortenerService service = new UrlShortenerService();

        String shortUrl = service.shortenUrl("https://example.com");
        String original = service.getOriginalUrl(shortUrl);

        assertEquals("https://example.com", original);
    }

    @Test
    void testGetOriginalUrlNullInput() {
        UrlShortenerService service = new UrlShortenerService();

        assertNull(service.getOriginalUrl(null));
    }

    @Test
    void testGetOriginalUrlInvalidPrefix() {
        UrlShortenerService service = new UrlShortenerService();

        assertNull(service.getOriginalUrl("https://invalid.com/abc"));
    }

    @Test
    void testGetOriginalUrlValidFormatButNotStored() {
        UrlShortenerService service = new UrlShortenerService();

        String fakeUrl = "https://short.ly/unknown";
        assertNull(service.getOriginalUrl(fakeUrl));
    }

    @Test
    void testTotalUrlsAfterMultipleInsertions() {
        UrlShortenerService service = new UrlShortenerService();

        service.shortenUrl("https://a.com");
        service.shortenUrl("https://b.com");
        service.shortenUrl("https://c.com");

        assertEquals(3, service.totalUrls());
    }
}
