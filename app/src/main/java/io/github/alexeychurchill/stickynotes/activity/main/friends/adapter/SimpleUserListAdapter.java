package io.github.alexeychurchill.stickynotes.activity.main.friends.adapter;

import io.github.alexeychurchill.stickynotes.model.User;

/**
 * Simple user list adapter
 */

public class SimpleUserListAdapter extends UserListAdapter<User> {
    @Override
    protected User getUser(User user) {
        return user;
    }

    @Override
    protected String getInfo(User user) {
        return null;
    }
}
