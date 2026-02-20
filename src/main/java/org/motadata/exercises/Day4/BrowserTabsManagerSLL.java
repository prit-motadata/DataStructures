package org.motadata.exercises.Day4;

import org.motadata.datastructures.linkedlist.SinglyLinkedList;

/**
 * Manages browser tabs using a singly linked list.
 *
 * <p>
 * Supports basic operations like opening, closing, and searching for tabs.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class BrowserTabsManagerSLL {

    private final SinglyLinkedList<String> tabs;

    /**
     * Creates an empty browser tab manager using a singly linked list.
     */
    public BrowserTabsManagerSLL() {
        this.tabs = new SinglyLinkedList<>();
    }

    /**
     * Opens a new tab and adds it to the end of the list.
     *
     * @param tabName the name of the tab to open
     */
    public void openTab(String tabName) {
        tabs.addLast(tabName);
    }

    /**
     * Closes the tab with the specified name.
     *
     * @param tabName the name of the tab to close
     * @return {@code true} if the tab was found and closed, {@code false} otherwise
     */
    public boolean closeTab(String tabName) {
        return tabs.remove(tabName);
    }

    /**
     * Searches for a tab by name.
     *
     * @param tabName the name of the tab to search for
     * @return {@code true} if the tab exists, {@code false} otherwise
     */
    public boolean search(String tabName) {
        return tabs.contains(tabName);
    }

    /**
     * Displays all current open tabs to the standard output.
     */
    public void displayTabs() {
        System.out.println("Open browser tabs:");
        tabs.display();
    }
}
