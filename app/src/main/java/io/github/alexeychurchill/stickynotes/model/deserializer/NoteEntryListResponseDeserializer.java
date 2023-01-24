package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.github.alexeychurchill.stickynotes.model.OldNoteEntry;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Note entry list response deserializer
 */

public class NoteEntryListResponseDeserializer implements JsonDeserializer<ServiceResponse<List<OldNoteEntry>>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<List<OldNoteEntry>>>() {}.getType();

    private static final String TAG = "Wrrr";

    @Override
    public ServiceResponse<List<OldNoteEntry>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootObject = json.getAsJsonObject();
        ServiceResponse.Builder<List<OldNoteEntry>> builder = new ServiceResponse.Builder<>();
        if (rootObject.has("response")) {
            JsonObject noteListResponseObject = rootObject.getAsJsonObject("response");
            List<OldNoteEntry> noteEntries = null;
            if (noteListResponseObject.has("notes")) {
                JsonArray noteEntryArray = noteListResponseObject.get("notes").getAsJsonArray();
                noteEntries = context.deserialize(noteEntryArray, NoteEntryListDeserializer.TYPE);
            }
            builder.setData(noteEntries);
        }
        if (rootObject.has("error")) {
            JsonObject errorObject = rootObject.getAsJsonObject("error");
            int code = 0;
            String message = "";
            if (errorObject.has("code")) {
                code = errorObject.get("code").getAsInt();
            }
            if (errorObject.has("message")) {
                message = errorObject.get("message").getAsString();
            }
            builder.setError(code, message);
        }
        return builder.create();
    }
}
