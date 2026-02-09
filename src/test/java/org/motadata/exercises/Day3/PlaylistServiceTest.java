package org.motadata.exercises.Day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistServiceTest {

    private PlaylistService playlist;

    @BeforeEach
    void setUp() {
        playlist = new PlaylistService();
    }

    @Test
    void shouldAddSongAtBeginning() {
        playlist.addSongAtBeginning("Perfect");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        playlist.displayPlaylist();

        assertTrue(out.toString().contains("Perfect"));
    }

    @Test
    void shouldAddSongAtEnd() {
        playlist.addSongAtEnd("Believer");
        playlist.addSongAtEnd("Shape of You");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        playlist.displayPlaylist();

        String output = out.toString();
        assertTrue(output.contains("Believer"));
        assertTrue(output.contains("Shape of You"));
    }

    @Test
    void shouldDeleteExistingSong() {
        playlist.addSongAtEnd("Song1");

        boolean deleted = playlist.deleteSong("Song1");

        assertTrue(deleted);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingSong() {
        playlist.addSongAtEnd("Song1");

        boolean deleted = playlist.deleteSong("SongX");

        assertFalse(deleted);
    }

    @Test
    void shouldDisplayEmptyPlaylist() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        playlist.displayPlaylist();

        assertTrue(out.toString().contains("Playlist"));
    }
}
