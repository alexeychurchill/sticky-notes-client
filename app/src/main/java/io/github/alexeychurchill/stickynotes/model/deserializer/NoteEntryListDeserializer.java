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

import io.github.alexeychurchill.stickynotes.model.JsonNoteEntry;

/**
 * Note entry list deserializer
 */

public class NoteEntryListDeserializer implements JsonDeserializer<List<JsonNoteEntry>> {
    public static final Type TYPE = new TypeToken<List<JsonNoteEntry>>() {}.getType();

    @Override
    public List<JsonNoteEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonArray()) {
            return null;
        }
        JsonArray noteEntryArray = json.getAsJsonArray();
        List<JsonNoteEntry> noteEntries = new ArrayList<>();
        for (JsonElement jsonElement : noteEntryArray) {
            JsonNoteEntry noteEntry = context.deserialize(jsonElement, JsonNoteEntry.class);
            noteEntries.add(noteEntry);
        }
        return noteEntries;
    }
}
