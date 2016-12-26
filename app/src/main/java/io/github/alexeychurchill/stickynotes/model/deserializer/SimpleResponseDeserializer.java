package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Deserializer for simple response: code, message
 */

public class SimpleResponseDeserializer implements JsonDeserializer<ServiceResponse<Object>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<Object>>() {}.getType();

    @Override
    public ServiceResponse<Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        boolean error = false;
        JsonObject rootObject = json.getAsJsonObject();
        JsonObject responseOrErrorObject = null;
        if (rootObject.has("error")) {
            error = true;
            responseOrErrorObject = rootObject.getAsJsonObject("error");
        }
        if (rootObject.has("response")) {
            error = false;
            responseOrErrorObject = rootObject.getAsJsonObject("response");
        }
        if (responseOrErrorObject == null) {
            return null;
        }
        int code = 0;
        if (responseOrErrorObject.has("code")) {
            code = responseOrErrorObject.get("code").getAsInt();
        }
        String message = "";
        if (responseOrErrorObject.has("message")) {
            message = responseOrErrorObject.get("message").getAsString();
        }
        ServiceResponse.Builder<Object> builder = new ServiceResponse.Builder<>();
        return builder.setResponse(error, code, message)
                .setData(null)
                .create();
    }
}
