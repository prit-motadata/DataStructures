package org.motadata.exercises.Day3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistServiceArrayDynamicTest {
    @Test
    void testEmptyPlaylistNextSong() {
        PlaylistServiceArrayDynamic playlist = new PlaylistServiceArrayDynamic();
        assertNull(playlist.nextSong());
    }

    @Test
    void testAddAndPlaySequentially() {
        PlaylistServiceArrayDynamic playlist = new PlaylistServiceArrayDynamic();

        playlist.addSong("Song1");
        playlist.addSong("Song2");
        playlist.addSong("Song3");

        assertEquals("Song1", playlist.nextSong());
        assertEquals("Song2", playlist.nextSong());
        assertEquals("Song3", playlist.nextSong());

        // wrap-around
        assertEquals("Song1", playlist.nextSong());
    }

    @Test
    void testEnableShufflePlaysAllSongs() {
        PlaylistServiceArrayDynamic playlist = new PlaylistServiceArrayDynamic();

        playlist.addSong("A");
        playlist.addSong("B");
        playlist.addSong("C");

        playlist.enableShuffle();

        String first = playlist.nextSong();
        String second = playlist.nextSong();
        String third = playlist.nextSong();

        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);

        // All three should be unique
        assertNotEquals(first, second);
        assertNotEquals(second, third);
        assertNotEquals(first, third);

        // wrap-around in shuffle mode
        assertNotNull(playlist.nextSong());
    }

    @Test
    void testDisableShuffleResetsOrder() {
        PlaylistServiceArrayDynamic playlist = new PlaylistServiceArrayDynamic();

        playlist.addSong("X");
        playlist.addSong("Y");

        playlist.enableShuffle();
        playlist.nextSong(); // consume one

        playlist.disableShuffle();

        // Should restart from beginning
        assertEquals("X", playlist.nextSong());
    }

    @Test
    void testSingleSongShuffle() {
        PlaylistServiceArrayDynamic playlist = new PlaylistServiceArrayDynamic();

        playlist.addSong("OnlyOne");

        playlist.enableShuffle();

        assertEquals("OnlyOne", playlist.nextSong());
        assertEquals("OnlyOne", playlist.nextSong()); // wrap
    }

    @Test
    void testMergeWithNullPlaylist() {
        PlaylistServiceArrayDynamic playlist =
                new PlaylistServiceArrayDynamic();

        playlist.addSong("A");

        playlist.merge(null); // should not throw

        assertEquals(1, playlist.totalSongs());
        assertEquals("A", playlist.nextSong());
    }

    @Test
    void testMergeWithEmptyPlaylist() {
        PlaylistServiceArrayDynamic playlist1 =
                new PlaylistServiceArrayDynamic();

        PlaylistServiceArrayDynamic playlist2 =
                new PlaylistServiceArrayDynamic();

        playlist1.addSong("A");

        playlist1.merge(playlist2); // empty merge

        assertEquals(1, playlist1.totalSongs());
        assertEquals("A", playlist1.nextSong());
    }

    @Test
    void testNormalMergeWithoutShuffle() {
        PlaylistServiceArrayDynamic playlist1 =
                new PlaylistServiceArrayDynamic();

        PlaylistServiceArrayDynamic playlist2 =
                new PlaylistServiceArrayDynamic();

        playlist1.addSong("A");
        playlist1.addSong("B");

        playlist2.addSong("C");
        playlist2.addSong("D");

        playlist1.merge(playlist2);

        assertEquals(4, playlist1.totalSongs());

        assertEquals("A", playlist1.nextSong());
        assertEquals("B", playlist1.nextSong());
        assertEquals("C", playlist1.nextSong());
        assertEquals("D", playlist1.nextSong());
    }

    @Test
    void testMergeWhileShuffleEnabled() {
        PlaylistServiceArrayDynamic playlist1 =
                new PlaylistServiceArrayDynamic();

        PlaylistServiceArrayDynamic playlist2 =
                new PlaylistServiceArrayDynamic();

        playlist1.addSong("A");
        playlist1.addSong("B");

        playlist2.addSong("C");

        playlist1.enableShuffle(); // activates shuffleMode

        playlist1.merge(playlist2); // should regenerate shuffle order

        assertEquals(3, playlist1.totalSongs());

        // Ensure all songs are playable and none are null
        String s1 = playlist1.nextSong();
        String s2 = playlist1.nextSong();
        String s3 = playlist1.nextSong();

        assertNotNull(s1);
        assertNotNull(s2);
        assertNotNull(s3);

        // All unique (since only 3 songs)
        assertNotEquals(s1, s2);
        assertNotEquals(s2, s3);
        assertNotEquals(s1, s3);
    }
}
