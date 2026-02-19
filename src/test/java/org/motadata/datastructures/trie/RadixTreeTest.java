package org.motadata.datastructures.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RadixTreeTest {

    private RadixTree tree;

    @BeforeEach
    void setUp() {
        tree = new RadixTree();
    }

    // -------- INSERT & SEARCH --------

    @Test
    void shouldInsertAndSearchWord() {
        tree.insert("amazon");

        assertTrue(tree.search("amazon"));
    }

    @Test
    void shouldReturnFalseForNonExistingWord() {
        tree.insert("amazon");

        assertFalse(tree.search("amaz"));
        assertFalse(tree.search("google"));
    }

    @Test
    void shouldHandleMultipleInsertions() {
        tree.insert("amazon");
        tree.insert("amazing");
        tree.insert("amber");

        assertTrue(tree.search("amazon"));
        assertTrue(tree.search("amazing"));
        assertTrue(tree.search("amber"));
    }

    // -------- EDGE SPLITTING (CRITICAL FOR RADIX) --------

    @Test
    void shouldSplitEdgeCorrectlyWhenPartialMatchOccurs() {
        tree.insert("amazon");
        tree.insert("amazing"); // forces split at "amaz"

        assertTrue(tree.search("amazon"));
        assertTrue(tree.search("amazing"));
    }

    @Test
    void shouldHandleWordBeingPrefixOfAnother() {
        tree.insert("car");
        tree.insert("cart");

        assertTrue(tree.search("car"));
        assertTrue(tree.search("cart"));
    }

    @Test
    void shouldHandleAnotherWordExtendingExistingPrefix() {
        tree.insert("carpet");
        tree.insert("car");

        assertTrue(tree.search("car"));
        assertTrue(tree.search("carpet"));
    }

    // -------- PREFIX SEARCH --------

    @Test
    void shouldReturnMultipleWordsForPrefix() {
        tree.insert("amazon");
        tree.insert("amazing");
        tree.insert("amber");
        tree.insert("google");

        Set<String> result = tree.searchByPrefix("am");

        assertEquals(3, result.size());
        assertTrue(result.contains("amazon"));
        assertTrue(result.contains("amazing"));
        assertTrue(result.contains("amber"));
        assertFalse(result.contains("google"));
    }

    @Test
    void shouldReturnSingleMatchForPrefix() {
        tree.insert("alice");
        tree.insert("bob");

        Set<String> result = tree.searchByPrefix("ali");

        assertEquals(1, result.size());
        assertTrue(result.contains("alice"));
    }

    @Test
    void shouldReturnEmptySetWhenPrefixNotFound() {
        tree.insert("alice");

        Set<String> result = tree.searchByPrefix("zz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllWordsForEmptyPrefix() {
        tree.insert("alice");
        tree.insert("bob");

        Set<String> result = tree.searchByPrefix("");

        assertEquals(2, result.size());
        assertTrue(result.contains("alice"));
        assertTrue(result.contains("bob"));
    }

    // -------- DELETE --------

    @Test
    void shouldDeleteExistingWord() {
        tree.insert("amazon");

        assertTrue(tree.delete("amazon"));
        assertFalse(tree.search("amazon"));
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingWord() {
        tree.insert("amazon");

        assertFalse(tree.delete("google"));
    }

    @Test
    void shouldDeleteWordWithoutAffectingOthers() {
        tree.insert("amazon");
        tree.insert("amazing");

        tree.delete("amazon");

        assertFalse(tree.search("amazon"));
        assertTrue(tree.search("amazing"));
    }

    @Test
    void shouldDeletePrefixWordButKeepExtendedWord() {
        tree.insert("car");
        tree.insert("cart");

        tree.delete("car");

        assertFalse(tree.search("car"));
        assertTrue(tree.search("cart"));
    }

    // -------- EDGE CASES --------

    @Test
    void searchShouldReturnFalseForEmptyTree() {
        assertFalse(tree.search("anything"));
    }

    @Test
    void deleteShouldReturnFalseForEmptyTree() {
        assertFalse(tree.delete("anything"));
    }

    @Test
    void prefixSearchShouldReturnEmptyForEmptyTree() {
        Set<String> result = tree.searchByPrefix("a");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
