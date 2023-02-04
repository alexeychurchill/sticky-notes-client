package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Shared note class
 */
@Deprecated
public class SharedNoteFull {
    @SerializedName("note")
    private NoteFull note;
    @SerializedName("edit_permission")
    private int editPermission;

    public NoteFull getNote() {
        return note;
    }

    public boolean canEdit() {
        return editPermission > 0;
    }
}
