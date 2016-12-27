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

import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Friend request list response deserializer
 */

public class FriendRequestListResponseDeserializer implements JsonDeserializer<ServiceResponse<List<FriendRequest>>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<List<FriendRequest>>>() {}.getType();
    @Override
    public ServiceResponse<List<FriendRequest>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ServiceResponse.Builder<List<FriendRequest>> builder = new ServiceResponse.Builder<>();
        JsonObject rootObject = json.getAsJsonObject();
        if (rootObject.has("response")) {
            JsonArray responseArray = rootObject.get("response").getAsJsonArray();
            List<FriendRequest> friendRequests =
                    context.deserialize(responseArray, FriendRequestListDeserializer.TYPE);
            builder.setData(friendRequests);
        }
        if (rootObject.has("error")) {
            int code = 0;
            String message = "";
            JsonObject errorObject = rootObject.get("error").getAsJsonObject();
            if (errorObject.has("code")) {
                code = errorObject.get("code").getAsInt();
            }
            if (errorObject.has("message")) {
                message = errorObject.get("message").getAsString();
            }
            builder.setMessage(code, message);
        }
        return builder.create();
    }
}
