package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.alexeychurchill.stickynotes.model.LoginResult;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Login result deserializer
 */

public class LoginResultDeserializer implements JsonDeserializer<ServiceResponse<LoginResult>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<LoginResult>>() {}.getType();

    @Override
    public ServiceResponse<LoginResult> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootObject = json.getAsJsonObject();
        ServiceResponse.Builder<LoginResult> builder = new ServiceResponse.Builder<>();
        if (rootObject.has("response")) {
            JsonObject tokenObject = rootObject.getAsJsonObject("response");
            LoginResult loginResult = context.deserialize(tokenObject, LoginResult.class);
            builder.setData(loginResult);
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
