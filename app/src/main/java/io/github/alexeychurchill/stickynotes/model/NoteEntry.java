package io.github.alexeychurchill.stickynotes.model;

import java.util.Calendar;

/**
 * Note entry class
 */

public class NoteEntry {
    private int id;
    private String title;
    private String subject;
    private String text;
    private int createdDate;
    private int changedDate;
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

    public String getText() {
        return text;
    }

    public Calendar getCreatedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdDate / 1000);
        return calendar;
    }

    public Calendar getChangedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(changedDate / 1000);
        return calendar;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
