package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.SharedNoteFull;

/**
 * Shared note response deserializer
 */

public class SharedNoteResponseDeserializer implements JsonDeserializer<ServiceResponse<SharedNoteFull>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<SharedNoteFull>>() {}.getType();
    @Override
    public ServiceResponse<SharedNoteFull> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ServiceResponse.Builder<SharedNoteFull> builder = new ServiceResponse.Builder<>();
        JsonObject rootObject = json.getAsJsonObject();
        if (rootObject.has("response")) {
            JsonObject responseObject = rootObject.get("response").getAsJsonObject();
            SharedNoteFull sharedNoteFull = context.deserialize(responseObject, SharedNoteFull.class);
            builder.setData(sharedNoteFull);
        }
        if (rootObject.has("error")) {
            JsonObject errorObject = rootObject.get("error").getAsJsonObject();
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
