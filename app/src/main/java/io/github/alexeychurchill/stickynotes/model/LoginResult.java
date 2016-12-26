package io.github.alexeychurchill.stickynotes.model;

/**
 * Login result class
 */

public class LoginResult {
    private String accessToken;

    public LoginResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
