package org.motadata.exercises.Day6.guided;

import java.util.Objects;

/**
 * A record representing a chat message.
 *
 * @param from    username of the sender
 * @param to      username of the recipient
 * @param content actual content of the message
 * @author prit.thakkar@motadata.com
 */
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
