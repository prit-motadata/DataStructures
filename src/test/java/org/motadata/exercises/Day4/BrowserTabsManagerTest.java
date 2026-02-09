package org.motadata.exercises.Day4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BrowserTabsManagerTest {

    private BrowserTabsManager manager;

    @BeforeEach
    void setUp() {
        manager = new BrowserTabsManager();
    }

    @Test
    void shouldOpenTabsSuccessfully() {
        manager.openTab("Google");
        manager.openTab("YouTube");

        assertTrue(manager.isTabOpen("Google"));
        assertTrue(manager.isTabOpen("YouTube"));
    }

    @Test
    void shouldCloseExistingTab() {
        manager.openTab("GitHub");

        boolean closed = manager.closeTab("GitHub");

        assertTrue(closed);
        assertFalse(manager.isTabOpen("GitHub"));
    }

    @Test
    void shouldReturnFalseWhenClosingNonExistingTab() {
        manager.openTab("Google");

        boolean closed = manager.closeTab("StackOverflow");

        assertFalse(closed);
    }

    @Test
    void shouldSearchTabCorrectly() {
        manager.openTab("Docs");

        assertTrue(manager.isTabOpen("Docs"));
        assertFalse(manager.isTabOpen("Mail"));
    }

    @Test
    void shouldDisplayTabsWhenNotEmpty() {
        manager.openTab("Google");
        manager.openTab("YouTube");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        manager.displayTabs();

        String output = out.toString();
        assertTrue(output.contains("Open browser tabs"));
        assertTrue(output.contains("Google"));
        assertTrue(output.contains("YouTube"));
        assertTrue(output.contains("END"));
    }

    @Test
    void shouldDisplayMessageWhenNoTabsOpen() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        manager.displayTabs();

        String output = out.toString();
        assertTrue(output.contains("Open browser tabs"));
        assertTrue(output.contains("List is empty"));
    }
}
