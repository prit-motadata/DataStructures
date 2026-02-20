package org.motadata.exercises.Day6.optimized;

/**
 * A record representing a chat message in an optimized conversation.
 *
 * @param sender    the username of the message sender
 * @param content   the textual content of the message
 * @param timestamp the time when the message was sent (milliseconds since
 *                  epoch)
 * @author prit.thakkar@motadata.com
 */
public record Message(String sender, String content, long timestamp) {
}
