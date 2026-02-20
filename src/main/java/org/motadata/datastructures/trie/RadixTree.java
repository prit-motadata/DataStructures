package org.motadata.datastructures.trie;

import java.util.*;

/**
 * A Radix Tree (also known as a compact trie) implementation for efficient
 * string storage and retrieval.
 *
 * <p>
 * Unlike a standard Trie, nodes with only one child are merged with their
 * parents,
 * saving space and improving lookup performance by reducing the number of nodes
 * visited.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class RadixTree {

    /**
     * Internal node representing a prefix or word in the Radix Tree.
     */
    private static class Node {
        Map<String, Node> children = new HashMap<>();
        boolean isEnd;
    }

    private final Node root = new Node();

    // ---------------- INSERT ----------------

    /**
     * Inserts a word into the Radix Tree.
     *
     * <p>
     * If the word already exists, no changes are made. If it shares a common prefix
     * with an existing edge, the edge is split accordingly.
     * </p>
     *
     * @param word the word to be inserted
     */
    public void insert(String word) {
        insert(root, word);
    }

    private void insert(Node current, String word) {

        for (String edge : new ArrayList<>(current.children.keySet())) {

            int commonPrefixLength = commonPrefix(edge, word);

            if (commonPrefixLength == 0)
                continue;

            // Case 1: Edge fully matches
            if (commonPrefixLength == edge.length()) {
                String remainingWord = word.substring(commonPrefixLength);
                insert(current.children.get(edge), remainingWord);
                return;
            }

            // Case 2: Partial match → split edge
            String commonPrefix = edge.substring(0, commonPrefixLength);
            String remainingEdge = edge.substring(commonPrefixLength);
            String remainingWord = word.substring(commonPrefixLength);

            Node oldChild = current.children.remove(edge);
            Node newNode = new Node();

            // Attach old edge remainder
            newNode.children.put(remainingEdge, oldChild);

            // If word finished here
            if (remainingWord.isEmpty()) {
                newNode.isEnd = true;
            } else {
                insert(newNode, remainingWord);
            }

            current.children.put(commonPrefix, newNode);
            return;
        }

        // No matching edge → add new
        Node newNode = new Node();
        newNode.isEnd = true;
        current.children.put(word, newNode);
    }

    // ---------------- SEARCH ----------------

    /**
     * Searches for a word in the Radix Tree.
     *
     * @param word the word to search for
     * @return {@code true} if the word exists and is marked as an end node,
     *         {@code false} otherwise
     */
    public boolean search(String word) {
        return search(root, word);
    }

    private boolean search(Node current, String word) {

        for (Map.Entry<String, Node> entry : current.children.entrySet()) {
            String edge = entry.getKey();

            if (word.startsWith(edge)) {
                String remaining = word.substring(edge.length());
                if (remaining.isEmpty()) {
                    return entry.getValue().isEnd;
                }
                return search(entry.getValue(), remaining);
            }
        }

        return false;
    }

    // ---------------- PREFIX SEARCH ----------------

    /**
     * Retrieves all words in the tree that start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a set of all words matching the prefix
     */
    public Set<String> searchByPrefix(String prefix) {
        Set<String> result = new HashSet<>();
        searchPrefix(root, prefix, "", result);
        return result;
    }

    private void searchPrefix(Node current,
            String prefix,
            String path,
            Set<String> result) {

        for (Map.Entry<String, Node> entry : current.children.entrySet()) {
            String edge = entry.getKey();
            Node child = entry.getValue();

            String combined = path + edge;

            if (combined.startsWith(prefix)) {
                collectAll(child, combined, result);
            } else if (prefix.startsWith(combined)) {
                searchPrefix(child, prefix, combined, result);
            }
        }
    }

    private void collectAll(Node node,
            String path,
            Set<String> result) {

        if (node.isEnd) {
            result.add(path);
        }

        for (Map.Entry<String, Node> entry : node.children.entrySet()) {
            collectAll(entry.getValue(),
                    path + entry.getKey(),
                    result);
        }
    }

    // ---------------- DELETE (Optional basic version) ----------------

    /**
     * Removes a word from the Radix Tree if it exists.
     *
     * @param word the word to be deleted
     * @return {@code true} if the word was found and removed, {@code false}
     *         otherwise
     */
    public boolean delete(String word) {
        return delete(root, word);
    }

    private boolean delete(Node current, String word) {

        for (Map.Entry<String, Node> entry : current.children.entrySet()) {
            String edge = entry.getKey();
            Node child = entry.getValue();

            if (word.startsWith(edge)) {
                String remaining = word.substring(edge.length());

                if (remaining.isEmpty()) {
                    if (!child.isEnd)
                        return false;
                    child.isEnd = false;

                    if (child.children.isEmpty()) {
                        current.children.remove(edge);
                    }

                    return true;
                }

                return delete(child, remaining);
            }
        }

        return false;
    }

    // ---------------- Utility ----------------

    /**
     * Calculates the length of the common prefix between two strings.
     *
     * @param s1 first string
     * @param s2 second string
     * @return length of the shared prefix
     */
    private int commonPrefix(String s1, String s2) {
        int len = Math.min(s1.length(), s2.length());
        int i = 0;
        while (i < len && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }
}
