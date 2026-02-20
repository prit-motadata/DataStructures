package org.motadata.exercises.Day6.guided;

import java.util.Objects;

public record Message(String from, String to, String content) {

    public Message {
        Objects.requireNonNull(from, "Sender cannot be null");
        Objects.requireNonNull(to, "Receiver cannot be null");
        Objects.requireNonNull(content, "Content cannot be null");

        if (content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be blank");
        }
    }

    @Override
    public String toString() {
        return from + " -> " + to + ": " + content;
    }
}
