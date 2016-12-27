package io.github.alexeychurchill.stickynotes.fragment;

import android.view.View;

import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import retrofit2.Call;

/**
 * Incoming friend list request
 */

public class IncomingFriendRequestListFragment extends FriendRequestListFragment {
    private String mActionAcceptTitle = "";
    private String mActionRejectTitle = "";

    @Override
    public String getActionOneTitle() { // Accept
        return mActionAcceptTitle;
    }

    @Override
    public String getActionTwoTitle() { // Reject
        return mActionRejectTitle;
    }

    @Override
    public boolean userListNeedActionOne() {
        return true;
    }

    @Override
    public boolean userListNeedActionTwo() {
        return true;
    }

    @Override
    protected Call<ServiceResponse<Object>> getActionOneCall(FriendRequest friendRequest) {
        if (getApi() == null || getAccessToken() == null) {
            return null;
        }
        return getApi().friendAcceptRequest(getAccessToken(), friendRequest.getId());
    }

    @Override
    protected Call<ServiceResponse<Object>> getActionTwoCall(FriendRequest friendRequest) {
        if (getApi() == null || getAccessToken() == null) {
            return null;
        }
        return getApi().friendDeleteRequest(getAccessToken(), friendRequest.getId());
    }

    @Override
    protected void onInit(View view) {
        super.onInit(view);
        // Actions titles
        mActionAcceptTitle = getString(R.string.text_button_accept);
        mActionRejectTitle = getString(R.string.text_button_reject);
    }

    @Override
    protected Call<ServiceResponse<List<FriendRequest>>> getLoadDataPageCall(int page) {
        if (getApi() == null || getAccessToken() == null) {
            return null;
        }
        return getApi().friendGetRequests(getAccessToken(), page);
    }
}
