package org.motadata.exercises.Day5;

import org.motadata.datastructures.hashmap.GenericBucketHashMap;
import org.motadata.datastructures.hashmap.Map;

import java.util.Set;

public class UserLoginService {

    private final Map<String, String> users;

    public UserLoginService() {
        this.users = new GenericBucketHashMap<>();
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
        return users.delete(username);
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
