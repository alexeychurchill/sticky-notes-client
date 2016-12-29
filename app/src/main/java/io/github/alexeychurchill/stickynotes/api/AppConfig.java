package io.github.alexeychurchill.stickynotes.api;

/**
 * Simple config for service
 */

public class AppConfig {
    public static final String APP_PACKAGE = "io.github.alexeychurchill.stickynotes";
    public static final String BASE_URL = "http://192.168.0.104:5000";
    public static final String SHARED_BASE_URL = APP_PACKAGE.concat(".BASE_URL");
    public static final String SHARED_ACCESS_TOKEN = APP_PACKAGE.concat(".ACCESS_TOKEN");
    public static final String APP_PREFERENCES = APP_PACKAGE.concat(".APP_PREFERENCES");
}
