package io.github.alexeychurchill.stickynotes.model.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.model.Comment;

/**
 * Comment list deserializer
 */

public class CommentListDeserializer implements JsonDeserializer<List<Comment>> {
    public static final Type TYPE = new TypeToken<List<Comment>>() {}.getType();

    @Override
    public List<Comment> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Comment> comments = new LinkedList<>();
        if (!json.isJsonArray()) {
            return comments;
        }
        JsonArray commentsArray = json.getAsJsonArray();
        for (JsonElement jsonComment : commentsArray) {
            Comment comment = context.deserialize(jsonComment, Comment.class);
            comments.add(comment);
        }
        return comments;
    }
}
