package org.motadata.datastructures.trie;

import java.util.*;

public class RadixTree {

    private static class Node {
        Map<String, Node> children = new HashMap<>();
        boolean isEnd;
    }

    private final Node root = new Node();

    // ---------------- INSERT ----------------

    public void insert(String word) {
        insert(root, word);
    }

    private void insert(Node current, String word) {

        for (String edge : new ArrayList<>(current.children.keySet())) {

            int commonPrefixLength = commonPrefix(edge, word);

            if (commonPrefixLength == 0) continue;

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
            }
            else if (prefix.startsWith(combined)) {
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
                    if (!child.isEnd) return false;
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

    private int commonPrefix(String s1, String s2) {
        int len = Math.min(s1.length(), s2.length());
        int i = 0;
        while (i < len && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }
}
