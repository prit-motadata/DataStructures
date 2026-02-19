package org.motadata.exercises.Day4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrowserTabsManagerCHMTest {

    @Test
    void testOpenAndSearchTabs() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM();

        manager.openTab("Google");
        manager.openTab("YouTube");

        assertTrue(manager.search("Google"));
        assertTrue(manager.search("YouTube"));
        assertFalse(manager.search("Facebook"));

        assertEquals(2, manager.totalTabs());
    }

    @Test
    void testCloseTabSuccess() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM();

        manager.openTab("Google");

        assertTrue(manager.closeTab("Google"));
        assertFalse(manager.search("Google"));
        assertEquals(0, manager.totalTabs());
    }

    @Test
    void testCloseTabFailure() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM();

        assertFalse(manager.closeTab("NonExisting"));
        assertEquals(0, manager.totalTabs());
    }

    @Test
    void testInsertionOrderMaintained() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM();

        manager.openTab("A");
        manager.openTab("B");
        manager.openTab("C");

        StringBuilder result = new StringBuilder();
        for (String tab : manager.getTabs()) {
            result.append(tab);
        }

        assertEquals("ABC", result.toString());
    }

    @Test
    void testAccessOrderMode() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM(true);

        manager.openTab("A");
        manager.openTab("B");
        manager.openTab("C");

        // Access A → should move to end
        assertTrue(manager.search("A"));

        StringBuilder result = new StringBuilder();
        for (String tab : manager.getTabs()) {
            result.append(tab);
        }

        assertEquals("BCA", result.toString());
    }

    @Test
    void testDisplayTabsDoesNotThrow() {
        BrowserTabsManagerCHM manager = new BrowserTabsManagerCHM();
        manager.openTab("Test");

        assertDoesNotThrow(manager::displayTabs);
    }
}
