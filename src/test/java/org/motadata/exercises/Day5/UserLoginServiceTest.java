package org.motadata.exercises.Day5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.motadata.common.factory.MapType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginServiceTest {

    private UserLoginService createService(MapType mapType) {
        return new UserLoginService(mapType);
    }

    // -------- registerUser tests --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldRegisterUserSuccessfully(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        boolean result = loginService.registerUser("alice", "password");

        assertTrue(result);
        assertEquals(1, loginService.totalUsers());
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldNotRegisterDuplicateUser(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        loginService.registerUser("alice", "password");

        boolean result = loginService.registerUser("alice", "newPassword");

        assertFalse(result);
        assertEquals(1, loginService.totalUsers());
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldThrowExceptionWhenUsernameIsNull(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.registerUser(null, "password")
        );

        assertEquals("Username and password cannot be null", ex.getMessage());
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldThrowExceptionWhenPasswordIsNull(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.registerUser("alice", null)
        );

        assertEquals("Username and password cannot be null", ex.getMessage());
    }

    // -------- authenticate tests --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldAuthenticateSuccessfullyWithCorrectCredentials(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        loginService.registerUser("bob", "secret");

        boolean authenticated = loginService.authenticate("bob", "secret");

        assertTrue(authenticated);
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldFailAuthenticationWithWrongPassword(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        loginService.registerUser("bob", "secret");

        boolean authenticated = loginService.authenticate("bob", "wrong");

        assertFalse(authenticated);
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldFailAuthenticationForNonExistingUser(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        boolean authenticated = loginService.authenticate("ghost", "password");

        assertFalse(authenticated);
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldFailAuthenticationWhenUsernameIsNull(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        boolean authenticated = loginService.authenticate(null, "password");

        assertFalse(authenticated);
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldFailAuthenticationWhenPasswordIsNull(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        boolean authenticated = loginService.authenticate("alice", null);

        assertFalse(authenticated);
    }

    // -------- deleteUser --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldDeleteExistingUser(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        loginService.registerUser("alice", "123");

        boolean deleted = loginService.deleteUser("alice");

        assertTrue(deleted);
        assertEquals(0, loginService.totalUsers());
        assertFalse(loginService.authenticate("alice", "123"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnFalseWhenDeletingNonExistingUser(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        assertFalse(loginService.deleteUser("unknown"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnFalseWhenDeletingNullUser(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        assertFalse(loginService.deleteUser(null));
    }

    // -------- getAllUsers --------

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnAllRegisteredUsers(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        loginService.registerUser("alice", "123");
        loginService.registerUser("bob", "456");
        loginService.registerUser("charlie", "789");

        Set<String> users = loginService.getAllUsers();

        assertEquals(3, users.size());
        assertTrue(users.contains("alice"));
        assertTrue(users.contains("bob"));
        assertTrue(users.contains("charlie"));
    }

    @ParameterizedTest
    @EnumSource(MapType.class)
    void shouldReturnEmptySetWhenNoUsersRegistered(MapType mapType) {
        UserLoginService loginService = createService(mapType);

        Set<String> users = loginService.getAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    // ------- Default Constructor -------

    @Test
    void shouldUseGenericMapByDefaultConstructor() {
        UserLoginService service = new UserLoginService();

        assertTrue(service.registerUser("alice", "123"));
        assertTrue(service.authenticate("alice", "123"));
        assertEquals(1, service.totalUsers());
    }
}
