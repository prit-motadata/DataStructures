package org.motadata.exercises.Day3;

import org.motadata.datastructures.array.ArrayDynamic;

/**
 * Service for managing a playlist of songs using a dynamic array.
 *
 * <p>
 * Supports adding songs, shuffle mode, and merging with another playlist.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class PlaylistServiceArrayDynamic {

    private final ArrayDynamic<String> songs;
    private int currentIndex;
    private boolean shuffleMode;
    private int[] shuffleOrder;

    /**
     * Creates an empty playlist service with shuffle mode disabled.
     */
    public PlaylistServiceArrayDynamic() {
        songs = new ArrayDynamic<>();
        currentIndex = 0;
        shuffleMode = false;
    }

    /**
     * Adds a song to the end of the playlist.
     *
     * @param song the song name to add
     * @throws IllegalArgumentException if song is {@code null}
     */
    public void addSong(String song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }
        songs.add(song);
    }

    /**
     * Enables shuffle mode and generates a random shuffle order.
     */
    public void enableShuffle() {
        shuffleMode = true;
        generateShuffleOrder();
    }

    /**
     * Disables shuffle mode and resets to the first song in original order.
     */
    public void disableShuffle() {
        shuffleMode = false;
        currentIndex = 0;
    }

    /**
     * Retrieves the next song to play, respecting shuffle mode.
     *
     * @return the name of the next song, or {@code null} if the playlist is empty
     */
    public String nextSong() {
        if (songs.size() == 0)
            return null;

        int index = shuffleMode
                ? shuffleOrder[currentIndex]
                : currentIndex;

        String song = songs.get(index);

        currentIndex++;
        if (currentIndex >= songs.size()) {
            currentIndex = 0;
        }

        return song;
    }

    /**
     * Generates a random shuffle order using the Fisher-Yates algorithm.
     */
    private void generateShuffleOrder() {
        shuffleOrder = new int[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            shuffleOrder[i] = i;
        }

        // Fisher–Yates shuffle
        for (int i = songs.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = shuffleOrder[i];
            shuffleOrder[i] = shuffleOrder[j];
            shuffleOrder[j] = temp;
        }

        currentIndex = 0;
    }

    /**
     * Merges another playlist into this one.
     *
     * @param other the playlist service to merge from
     */
    public void merge(PlaylistServiceArrayDynamic other) {
        if (other == null || other.totalSongs() == 0) {
            return;
        }

        int newSize = this.totalSongs() + other.totalSongs();

        // Ensure capacity once
        songs.ensureCapacity(newSize);

        for (int i = 0; i < other.totalSongs(); i++) {
            songs.add(other.songs.get(i));
        }

        // If shuffle mode is active, regenerate shuffle order
        if (shuffleMode) {
            generateShuffleOrder();
        }
    }

    /**
     * Returns the total number of songs in the playlist.
     *
     * @return song count
     */
    public int totalSongs() {
        return songs.size();
    }
}
