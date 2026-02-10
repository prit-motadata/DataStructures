package org.motadata.exercises.Day5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginServiceTest {

    private UserLoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new UserLoginService();
    }

    // -------- registerUser tests --------

    @Test
    void shouldRegisterUserSuccessfully() {
        boolean result = loginService.registerUser("alice", "password");

        assertTrue(result);
        assertEquals(1, loginService.totalUsers());
    }

    @Test
    void shouldNotRegisterDuplicateUser() {
        loginService.registerUser("alice", "password");

        boolean result = loginService.registerUser("alice", "newPassword");

        assertFalse(result);
        assertEquals(1, loginService.totalUsers());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.registerUser(null, "password")
        );

        assertEquals("Username and password cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.registerUser("alice", null)
        );

        assertEquals("Username and password cannot be null", ex.getMessage());
    }

    // -------- authenticate tests --------

    @Test
    void shouldAuthenticateSuccessfullyWithCorrectCredentials() {
        loginService.registerUser("bob", "secret");

        boolean authenticated = loginService.authenticate("bob", "secret");

        assertTrue(authenticated);
    }

    @Test
    void shouldFailAuthenticationWithWrongPassword() {
        loginService.registerUser("bob", "secret");

        boolean authenticated = loginService.authenticate("bob", "wrong");

        assertFalse(authenticated);
    }

    @Test
    void shouldFailAuthenticationForNonExistingUser() {
        boolean authenticated = loginService.authenticate("ghost", "password");

        assertFalse(authenticated);
    }

    @Test
    void shouldFailAuthenticationWhenUsernameIsNull() {
        boolean authenticated = loginService.authenticate(null, "password");

        assertFalse(authenticated);
    }

    @Test
    void shouldFailAuthenticationWhenPasswordIsNull() {
        boolean authenticated = loginService.authenticate("alice", null);

        assertFalse(authenticated);
    }
}
