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

import io.github.alexeychurchill.stickynotes.model.Comment;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;

/**
 * Comment list response deserializer
 */

public class CommentListResponseDeserializer implements JsonDeserializer<ServiceResponse<List<Comment>>> {
    public static final Type TYPE = new TypeToken<ServiceResponse<List<Comment>>>() {}.getType();

    @Override
    public ServiceResponse<List<Comment>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ServiceResponse.Builder<List<Comment>> builder = new ServiceResponse.Builder<>();
        JsonObject rootObject = json.getAsJsonObject();
        if (rootObject.has("response")) {
            JsonArray commentsArray = rootObject.get("response").getAsJsonArray();
            List<Comment> comments = context.deserialize(commentsArray, CommentListDeserializer.TYPE);
            builder.setData(comments);
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
            builder.setError(code, message);
        }
        return builder.create();
    }
}
