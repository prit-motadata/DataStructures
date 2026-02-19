package org.motadata.exercises.Day4;

import org.motadata.datastructures.linkedlist.SinglyLinkedList;

public class BrowserTabsManagerSLL {

    private final SinglyLinkedList<String> tabs;

    public BrowserTabsManagerSLL() {
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
    public boolean search(String tabName) {
        return tabs.contains(tabName);
    }

    // Display all tabs
    public void displayTabs() {
        System.out.println("Open browser tabs:");
        tabs.display();
    }
}
