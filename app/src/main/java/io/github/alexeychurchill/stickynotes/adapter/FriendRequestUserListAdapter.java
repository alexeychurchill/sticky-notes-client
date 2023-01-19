package io.github.alexeychurchill.stickynotes.adapter;

import java.util.Locale;

import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.JsonUser;

/**
 * Friend request list adapter
 */

public class FriendRequestUserListAdapter extends UserListAdapter<FriendRequest> {
    @Override
    protected JsonUser getUser(FriendRequest friendRequest) {
        return friendRequest.getUser();
    }

    @Override
    protected String getInfo(FriendRequest friendRequest) {
        if (friendRequest.getDate() == null) {
            return null;
        }
        return String.format(
                Locale.getDefault(),
                "%1$tH:%1$tM %1$td.%1$tm.%1$tY",
                friendRequest.getDate()
        );
    }
}
