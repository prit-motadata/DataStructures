package org.motadata.exercises.Day4;

import org.motadata.datastructures.hashmap.CustomLinkedHashMap;

public class BrowserTabsManagerCHM {

    private final CustomLinkedHashMap<String, Boolean> tabs;

    public BrowserTabsManagerCHM() {
        this(false);
    }

    // accessOrder = true → recently accessed tabs move to end
    public BrowserTabsManagerCHM(boolean accessOrder) {
        this.tabs = new CustomLinkedHashMap<>(16, accessOrder);
    }

    // Open new tab
    public void openTab(String tabName) {
        tabs.put(tabName, Boolean.TRUE);
    }

    // Close tab
    public boolean closeTab(String tabName) {
        return tabs.remove(tabName);
    }

    // Search tab
    public boolean search(String tabName) {
        return tabs.get(tabName) != null;
    }

    // Display all tabs in order
    public void displayTabs() {
        System.out.println("Open browser tabs:");
        for (String tab : tabs) {
            System.out.println(tab);
        }
    }

    public int totalTabs() {
        return tabs.size();
    }

    public CustomLinkedHashMap<String, Boolean> getTabs() {
        return tabs;
    }
}
