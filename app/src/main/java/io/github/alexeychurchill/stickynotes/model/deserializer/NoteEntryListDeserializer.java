package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.model.OldNoteEntry;

/**
 * Note entry list deserializer
 */

public class NoteEntryListDeserializer implements JsonDeserializer<List<OldNoteEntry>> {
    public static final Type TYPE = new TypeToken<List<OldNoteEntry>>() {}.getType();

    @Override
    public List<OldNoteEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonArray()) {
            return null;
        }
        JsonArray noteEntryArray = json.getAsJsonArray();
        List<OldNoteEntry> noteEntries = new ArrayList<>();
        for (JsonElement jsonElement : noteEntryArray) {
            OldNoteEntry noteEntry = context.deserialize(jsonElement, OldNoteEntry.class);
            noteEntries.add(noteEntry);
        }
        return noteEntries;
    }
}
