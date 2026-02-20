package org.motadata.exercises.Day3;

import org.motadata.datastructures.linkedlist.SinglyLinkedList;

/**
 * Service for managing a playlist of songs using a singly linked list.
 *
 * <p>
 * Allows adding songs at both ends and removing specific songs.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class PlaylistServiceSinglyLinkedList {

    private final SinglyLinkedList<String> playlist;

    /**
     * Creates an empty playlist service using a singly linked list.
     */
    public PlaylistServiceSinglyLinkedList() {
        this.playlist = new SinglyLinkedList<>();
    }

    /**
     * Adds a song to the beginning of the playlist.
     *
     * @param song the song name to add
     */
    public void addSongAtBeginning(String song) {
        playlist.addFirst(song);
    }

    /**
     * Adds a song to the end of the playlist.
     *
     * @param song the song name to add
     */
    public void addSongAtEnd(String song) {
        playlist.addLast(song);
    }

    /**
     * Removes a specific song from the playlist.
     *
     * @param song the song name to remove
     * @return {@code true} if the song was found and removed, {@code false}
     *         otherwise
     */
    public boolean deleteSong(String song) {
        return playlist.remove(song);
    }

    /**
     * Displays all songs in the playlist to the standard output.
     */
    public void displayPlaylist() {
        System.out.println("Playlist:");
        playlist.display();
    }
}
