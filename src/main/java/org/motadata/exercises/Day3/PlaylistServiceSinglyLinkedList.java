package org.motadata.exercises.Day3;

import org.motadata.datastructures.linkedlist.SinglyLinkedList;

public class PlaylistServiceSinglyLinkedList {

    private final SinglyLinkedList<String> playlist;

    public PlaylistServiceSinglyLinkedList() {
        this.playlist = new SinglyLinkedList<>();
    }

    public void addSongAtBeginning(String song) {
        playlist.addFirst(song);
    }

    public void addSongAtEnd(String song) {
        playlist.addLast(song);
    }

    public boolean deleteSong(String song) {
        return playlist.remove(song);
    }

    public void displayPlaylist() {
        System.out.println("Playlist:");
        playlist.display();
    }
}
