package io.github.alexeychurchill.stickynotes.contacts

import android.view.View
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.model.FriendRequest
import io.github.alexeychurchill.stickynotes.model.ServiceResponse
import retrofit2.Call

/**
 * Incoming friend list request
 */
class IncomingFriendRequestListFragment : FriendRequestListFragment() {
    private var mActionAcceptTitle = ""
    private var mActionRejectTitle = ""
    override fun getActionOneTitle(): String { // Accept
        return mActionAcceptTitle
    }

    override fun getActionTwoTitle(): String { // Reject
        return mActionRejectTitle
    }

    override fun userListNeedActionOne(): Boolean {
        return true
    }

    override fun userListNeedActionTwo(): Boolean {
        return true
    }

    override fun getActionOneCall(friendRequest: FriendRequest?): Call<ServiceResponse<Any>>? {
        return if (api == null || accessToken == null) {
            null
        } else api!!.friendAcceptRequest(accessToken, friendRequest!!.id)
    }

    override fun getActionTwoCall(friendRequest: FriendRequest?): Call<ServiceResponse<Any>>? {
        return if (api == null || accessToken == null) {
            null
        } else api!!.friendDeleteRequest(accessToken, friendRequest!!.id)
    }

    override fun onInit(view: View?) {
        super.onInit(view)
        // Actions titles
        mActionAcceptTitle = getString(R.string.text_button_accept)
        mActionRejectTitle = getString(R.string.text_button_reject)
    }

    override fun getLoadDataPageCall(page: Int): Call<ServiceResponse<List<FriendRequest>>>? {
        return if (api == null || accessToken == null) {
            null
        } else api!!.friendGetRequests(accessToken, page)
    }
}