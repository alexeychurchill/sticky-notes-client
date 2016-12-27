package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

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

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) date) * 1000);
        return calendar;
    }

    public User getUser() {
        return user;
    }
}
