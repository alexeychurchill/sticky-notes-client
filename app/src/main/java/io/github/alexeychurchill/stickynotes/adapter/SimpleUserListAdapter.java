package io.github.alexeychurchill.stickynotes.adapter;

import io.github.alexeychurchill.stickynotes.model.JsonUser;

/**
 * Simple user list adapter
 */

public class SimpleUserListAdapter extends UserListAdapter<JsonUser> {
    @Override
    protected JsonUser getUser(JsonUser user) {
        return user;
    }

    @Override
    protected String getInfo(JsonUser user) {
        return null;
    }
}
