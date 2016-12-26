package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.alexeychurchill.stickynotes.model.NoteFull;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Full note response deserializer
 */

public class NoteFullResponseDeserializer implements JsonDeserializer<ServiceResponse<NoteFull>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<NoteFull>>() {}.getType();

    @Override
    public ServiceResponse<NoteFull> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ServiceResponse.Builder<NoteFull> builder = new ServiceResponse.Builder<>();
        JsonObject rootObject = json.getAsJsonObject();
        if (rootObject.has("response")) {
            JsonObject fullNote = rootObject.getAsJsonObject("response");
            NoteFull noteFull = context.deserialize(fullNote, NoteFull.class);
            builder.setData(noteFull);
        }
        if (rootObject.has("error")) {
            int code = 0;
            String message = "";
            if (rootObject.has("code")) {
                code = rootObject.get("code").getAsInt();
            }
            if (rootObject.has("message")) {
                message = rootObject.get("message").getAsString();
            }
            builder.setError(code, message);
        }
        return builder.create();
    }
}
