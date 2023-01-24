package io.github.alexeychurchill.stickynotes.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Note entry class
 */
@Deprecated
public class OldNoteEntry {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("subject")
    private String subject;
    @SerializedName("creation_date")
    private int createdDate;
    @SerializedName("change_date")
    private int changedDate;
    @SerializedName("owner_id")
    private int ownerId;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public Calendar getCreatedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdDate * 1000);
        return calendar;
    }

    public Calendar getChangedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) changedDate) * 1000);
        return calendar;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
