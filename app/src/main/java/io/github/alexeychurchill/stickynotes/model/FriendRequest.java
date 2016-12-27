package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Friend request class
 */

public class FriendRequest {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private int date;
    @SerializedName("user")
    private User user;

    public int getId() {
        return id;
    }

    public int getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
