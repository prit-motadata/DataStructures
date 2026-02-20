package org.motadata.exercises.Day4;

import org.motadata.datastructures.hashmap.CustomLinkedHashMap;

/**
 * Manages browser tabs using a CustomLinkedHashMap.
 *
 * <p>
 * Supports opening, closing, searching, and displaying tabs. Can be configured
 * to maintain insertion order or access order.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class BrowserTabsManagerCHM {

    private final CustomLinkedHashMap<String, Boolean> tabs;

    /**
     * Creates a browser tab manager with default insertion order.
     */
    public BrowserTabsManagerCHM() {
        this(false);
    }

    /**
     * Creates a browser tab manager with specified ordering.
     *
     * @param accessOrder {@code true} for access-order (MRU), {@code false} for
     *                    insertion-order
     */
    public BrowserTabsManagerCHM(boolean accessOrder) {
        this.tabs = new CustomLinkedHashMap<>(16, accessOrder);
    }

    /**
     * Opens a new tab with the given name.
     *
     * @param tabName the name of the tab to open
     */
    public void openTab(String tabName) {
        tabs.put(tabName, Boolean.TRUE);
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
        return tabs.get(tabName) != null;
    }

    /**
     * Displays all open tabs in their respective order.
     */
    public void displayTabs() {
        System.out.println("Open browser tabs:");
        for (String tab : tabs) {
            System.out.println(tab);
        }
    }

    /**
     * Returns the total number of open tabs.
     *
     * @return total tab count
     */
    public int totalTabs() {
        return tabs.size();
    }

    /**
     * Returns the internal map containing the tabs.
     *
     * @return the tabs map
     */
    public CustomLinkedHashMap<String, Boolean> getTabs() {
        return tabs;
    }
}
