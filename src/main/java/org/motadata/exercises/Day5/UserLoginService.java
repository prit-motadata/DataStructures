package org.motadata.exercises.Day5;

import org.motadata.common.factory.map.MapFactory;
import org.motadata.common.factory.map.MapType;
import org.motadata.datastructures.hashmap.Map;
import org.motadata.datastructures.trie.RadixTree;

import java.util.Set;

public class UserLoginService {

    private final Map<String, String> users;
    private final RadixTree userIndex;

    public UserLoginService() {
        this.users = MapFactory.createDefault();
        this.userIndex = new RadixTree();
    }

    public UserLoginService(MapType mapType) {
        this.users = MapFactory.createMap(mapType);
        this.userIndex = new RadixTree();
    }

    // Add new user
    public boolean registerUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        if (users.containsKey(username)) {
            return false; // user already exists
        }

        users.put(username, password);
        userIndex.insert(username);
        return true;
    }

    // Authenticate login
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedPassword = users.get(username);
        return password.equals(storedPassword);
    }

    // Delete user
    public boolean deleteUser(String username) {
        if (username == null) {
            return false;
        }
        boolean removed = users.remove(username);
        if (removed) {
            userIndex.delete(username);
        }
        return removed;
    }

    // ---------- PREFIX SEARCH ----------

    public Set<String> searchUsersByPrefix(String prefix) {
        if (prefix == null) {
            return Set.of();
        }

        if (prefix.isEmpty()) {
            return users.keySet();   // <-- important
        }

        return userIndex.searchByPrefix(prefix);
    }

    // Get all registered users
    public Set<String> getAllUsers() {
        return users.keySet();
    }

    // Get total registered users
    public int totalUsers() {
        return users.size();
    }
}
