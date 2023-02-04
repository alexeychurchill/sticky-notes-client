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

import io.github.alexeychurchill.stickynotes.model.JsonUser;

/**
 * User list deserializer
 */

public class UserListDeserializer implements JsonDeserializer<List<JsonUser>> {
    public static final Type TYPE = new TypeToken<List<JsonUser>>() {}.getType();
    @Override
    public List<JsonUser> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<JsonUser> users = new ArrayList<>();
        if (!json.isJsonArray()) {
            return users;
        }
        JsonArray usersArray = json.getAsJsonArray();
        for (JsonElement element : usersArray) {
            JsonUser user = context.deserialize(element, JsonUser.class);
            users.add(user);
        }
        return users;
    }
}
