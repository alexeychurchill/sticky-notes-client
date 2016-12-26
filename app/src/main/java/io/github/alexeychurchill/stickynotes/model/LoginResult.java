package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Login result class
 */

public class LoginResult {
    @SerializedName("access_token")
    private String accessToken;

    public LoginResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
