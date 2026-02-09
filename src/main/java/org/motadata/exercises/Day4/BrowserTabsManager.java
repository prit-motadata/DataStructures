package org.motadata.exercises.Day4;

import org.motadata.datastructures.linkedlist.SinglyLinkedList;

public class BrowserTabsManager {

    private final SinglyLinkedList<String> tabs;

    public BrowserTabsManager() {
        this.tabs = new SinglyLinkedList<>();
    }

    // Open new tab
    public void openTab(String tabName) {
        tabs.addLast(tabName);
    }

    // Close tab
    public boolean closeTab(String tabName) {
        return tabs.remove(tabName);
    }

    // Search tab
    public boolean isTabOpen(String tabName) {
        return tabs.contains(tabName);
    }

    // Display all tabs
    public void displayTabs() {
        System.out.println("Open browser tabs:");
        tabs.display();
    }
}
