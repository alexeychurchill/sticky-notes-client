package io.github.alexeychurchill.stickynotes.fragment.users;

import android.view.View;

import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import retrofit2.Call;

/**
 * User friend request list fragment
 */

public class UserFriendRequestListFragment extends FriendRequestListFragment {
    private String mCancelTitle = "";

    @Override
    protected void onInit(View view) {
        super.onInit(view);
        mCancelTitle = getString(R.string.text_button_cancel);
    }

    @Override
    public String getActionOneTitle() {
        return mCancelTitle;
    }

    @Override
    public boolean userListNeedActionOne() {
        return true;
    }

    @Override
    protected Call<ServiceResponse<Object>> getActionOneCall(FriendRequest friendRequest) {
        if (getApi() == null || getAccessToken() == null) {
            return null;
        }
        return getApi().friendDeleteRequest(getAccessToken(), friendRequest.getId());
    }

    @Override
    protected Call<ServiceResponse<List<FriendRequest>>> getLoadDataPageCall(int page) {
        if (getApi() == null) {
            return null;
        }
        if (getAccessToken() == null) {
            return null;
        }
        return getApi().friendGetMyRequests(getAccessToken(), page);
    }
}
