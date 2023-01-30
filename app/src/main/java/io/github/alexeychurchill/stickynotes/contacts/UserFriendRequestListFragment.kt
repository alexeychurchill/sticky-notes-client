package io.github.alexeychurchill.stickynotes.contacts

import android.view.View
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.model.FriendRequest
import io.github.alexeychurchill.stickynotes.model.ServiceResponse
import retrofit2.Call

/**
 * User friend request list fragment
 */
class UserFriendRequestListFragment : FriendRequestListFragment() {
    private var mCancelTitle = ""
    override fun onInit(view: View?) {
        super.onInit(view)
        mCancelTitle = getString(R.string.text_button_cancel)
    }

    override fun getActionOneTitle(): String {
        return mCancelTitle
    }

    override fun userListNeedActionOne(): Boolean {
        return true
    }

    override fun getActionOneCall(friendRequest: FriendRequest?): Call<ServiceResponse<Any>>? {
        return if (api == null || accessToken == null) {
            null
        } else api!!.friendDeleteRequest(accessToken, friendRequest!!.id)
    }

    override fun getLoadDataPageCall(page: Int): Call<ServiceResponse<List<FriendRequest>>>? {
        if (api == null) {
            return null
        }
        return if (accessToken == null) {
            null
        } else api!!.friendGetMyRequests(accessToken, page)
    }
}