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

import io.github.alexeychurchill.stickynotes.model.User;

/**
 * User list deserializer
 */

public class UserListDeserializer implements JsonDeserializer<List<User>> {
    public static final Type TYPE = new TypeToken<List<User>>() {}.getType();
    @Override
    public List<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<User> users = new ArrayList<>();
        if (!json.isJsonArray()) {
            return users;
        }
        JsonArray usersArray = json.getAsJsonArray();
        for (JsonElement element : usersArray) {
            User user = context.deserialize(element, User.class);
            users.add(user);
        }
        return users;
    }
}
