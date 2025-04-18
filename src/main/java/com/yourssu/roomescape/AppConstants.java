package com.yourssu.roomescape;

/**
 * Application-wide constants
 */
public class AppConstants {
    // Cookie names
    public static final String TOKEN_COOKIE_NAME = "token";

    // Role names
    public static final String ROLE_ADMIN = "ADMIN";


    // Other constants can be added here as needed

    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}