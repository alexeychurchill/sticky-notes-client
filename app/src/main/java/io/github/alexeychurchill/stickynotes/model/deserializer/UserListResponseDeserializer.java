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

import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.JsonUser;

/**
 * User list response deserializer
 */

public class UserListResponseDeserializer implements JsonDeserializer<ServiceResponse<List<JsonUser>>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<List<JsonUser>>>() {}.getType();
    @Override
    public ServiceResponse<List<JsonUser>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ServiceResponse.Builder<List<JsonUser>> builder = new ServiceResponse.Builder<>();
        JsonObject rootObject = json.getAsJsonObject();
        if (rootObject.has("response")) {
            JsonArray usersArray = rootObject.get("response").getAsJsonArray();
            List<JsonUser> users = context.deserialize(usersArray, UserListDeserializer.TYPE);
            builder.setData(users);
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
