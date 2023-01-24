package io.github.alexeychurchill.stickynotes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Full note class (with note class)
 */

public class NoteFull extends JsonNoteEntry {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
