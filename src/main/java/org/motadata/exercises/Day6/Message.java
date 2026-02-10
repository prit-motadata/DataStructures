package org.motadata.exercises.Day6;

public record Message(String from, String to, String content) {

    @Override
    public String toString() {
        return from + " -> " + to + ": " + content;
    }
}
