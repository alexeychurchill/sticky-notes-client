package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.model.NoteEntry;

/**
 * Note entry list deserializer
 */

public class NoteEntryListDeserializer implements JsonDeserializer<List<NoteEntry>> {
    public static final Type TYPE = new TypeToken<List<NoteEntry>>() {}.getType();

    @Override
    public List<NoteEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonArray()) {
            return null;
        }
        JsonArray noteEntryArray = json.getAsJsonArray();
        List<NoteEntry> noteEntries = new ArrayList<>();
        for (JsonElement jsonElement : noteEntryArray) {
            NoteEntry noteEntry = context.deserialize(jsonElement, NoteEntry.class);
            noteEntries.add(noteEntry);
        }
        return noteEntries;
    }
}
