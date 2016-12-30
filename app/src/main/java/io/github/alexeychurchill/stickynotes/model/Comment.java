package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Comment object
 */

public class Comment {
    @SerializedName("id")
    private int id;
    @SerializedName("text")
    private String text;
    @SerializedName("note_id")
    private int noteId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("date")
    private int date;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getUserId() {
        return userId;
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) date)* 1000);
        return calendar;
    }
}
