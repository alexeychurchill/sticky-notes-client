package io.github.alexeychurchill.stickynotes.activity.main.friends.fragment;

import android.support.v4.app.Fragment;

import java.util.List;

import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import retrofit2.Call;

/**
 * User friend request list fragment
 */

public class UserFriendRequestListFragment extends FriendRequestListFragment {
    @Override
    protected Call<ServiceResponse<List<FriendRequest>>> getLoadDataPageCall(int page) {
        return null;
    }
}
