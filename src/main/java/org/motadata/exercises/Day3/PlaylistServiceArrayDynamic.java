package org.motadata.exercises.Day3;

import org.motadata.datastructures.array.ArrayDynamic;

public class PlaylistServiceArrayDynamic {

    private final ArrayDynamic<String> songs;
    private int currentIndex;
    private boolean shuffleMode;
    private int[] shuffleOrder;

    public PlaylistServiceArrayDynamic() {
        songs = new ArrayDynamic<>();
        currentIndex = 0;
        shuffleMode = false;
    }

    public void addSong(String song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }
        songs.add(song);
    }

    public void enableShuffle() {
        shuffleMode = true;
        generateShuffleOrder();
    }

    public void disableShuffle() {
        shuffleMode = false;
        currentIndex = 0;
    }

    public String nextSong() {
        if (songs.size() == 0) return null;

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

    private void generateShuffleOrder() {
        shuffleOrder = new int[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            shuffleOrder[i] = i;
        }

        // Fisher–Yates shuffle
        for (int i = songs.size() - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            int temp = shuffleOrder[i];
            shuffleOrder[i] = shuffleOrder[j];
            shuffleOrder[j] = temp;
        }

        currentIndex = 0;
    }

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

    public int totalSongs() {
        return songs.size();
    }
}

