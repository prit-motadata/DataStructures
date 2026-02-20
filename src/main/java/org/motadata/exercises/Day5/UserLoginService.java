package org.motadata.exercises.Day5;

import org.motadata.common.factory.map.MapFactory;
import org.motadata.common.factory.map.MapType;
import org.motadata.datastructures.hashmap.Map;
import org.motadata.datastructures.trie.RadixTree;

import java.util.Set;

/**
 * Service for managing user registration and authentication.
 *
 * <p>
 * Uses a Map for fast authentication and a RadixTree for efficient username
 * prefix searching.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class UserLoginService {

    private final Map<String, String> users;
    private final RadixTree userIndex;

    /**
     * Creates a new UserLoginService with default map implementation.
     */
    public UserLoginService() {
        this.users = MapFactory.createDefault();
        this.userIndex = new RadixTree();
    }

    /**
     * Creates a new UserLoginService with a specific map type.
     *
     * @param mapType the type of Map implementation to use for user storage
     */
    public UserLoginService(MapType mapType) {
        this.users = MapFactory.createMap(mapType);
        this.userIndex = new RadixTree();
    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username the unique username of the user
     * @param password the password for the user
     * @return {@code true} if registration was successful, {@code false} if the
     *         user already exists
     * @throws IllegalArgumentException if username or password is null
     */
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

    /**
     * Authenticates a user by checking if the given password matches the stored
     * one.
     *
     * @param username the username to authenticate
     * @param password the password to check
     * @return {@code true} if authentication is successful, {@code false} otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedPassword = users.get(username);
        return password.equals(storedPassword);
    }

    /**
     * Deletes a user from the system.
     *
     * @param username the username of the user to delete
     * @return {@code true} if the user was found and removed, {@code false}
     *         otherwise
     */
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

    /**
     * Searches for registered users whose usernames start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a set of matching usernames
     */
    public Set<String> searchUsersByPrefix(String prefix) {
        if (prefix == null) {
            return Set.of();
        }

        if (prefix.isEmpty()) {
            return users.keySet(); // <-- important
        }

        return userIndex.searchByPrefix(prefix);
    }

    /**
     * Returns a set of all registered usernames.
     *
     * @return all usernames
     */
    public Set<String> getAllUsers() {
        return users.keySet();
    }

    /**
     * Returns the total count of registered users.
     *
     * @return total user count
     */
    public int totalUsers() {
        return users.size();
    }
}
