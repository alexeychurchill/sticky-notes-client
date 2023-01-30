package io.github.alexeychurchill.stickynotes.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.adapter.SimpleUserListAdapter
import io.github.alexeychurchill.stickynotes.adapter.UserListAdapter.OnUserListActionListener
import io.github.alexeychurchill.stickynotes.api.AppConfig
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback
import io.github.alexeychurchill.stickynotes.dialog.FriendSearchDialogFragment
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener
import io.github.alexeychurchill.stickynotes.model.JsonUser
import io.github.alexeychurchill.stickynotes.model.ServiceResponse
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.UserListDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.UserListResponseDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Base friends list fragment
 */
class FriendsListFragment : Fragment(), OnUserListActionListener, View.OnClickListener {
    private var mActionOneTitle: String? = null
    private val mUserList: MutableList<JsonUser> = ArrayList()
    private var mPage = 0
    private var mAdapter: SimpleUserListAdapter? = null
    private var mSimpleResponseCallback: SimpleResponseCallback? = null
    private var mPBWait: ProgressBar? = null
    private var mRVFriends: RecyclerView? = null
    private var mFab: FloatingActionButton? = null
    private var mApi: StickyNotesApi? = null
    private var mAccessToken: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)
        mPBWait = view.findViewById<View>(R.id.pbWait) as ProgressBar
        // Action title
        mActionOneTitle = getString(R.string.text_button_delete)
        // FAB
        mFab = view.findViewById<View>(R.id.fab) as FloatingActionButton
        if (mFab != null) {
            mFab!!.setOnClickListener(this)
        }
        // RecyclerView
        val layoutManager = LinearLayoutManager(context)
        mAdapter = SimpleUserListAdapter()
        mRVFriends = view.findViewById<View>(R.id.rvFriends) as RecyclerView
        if (mRVFriends != null) {
            mRVFriends!!.layoutManager = layoutManager
            mRVFriends!!.adapter = mAdapter
            mRVFriends!!.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    loadDataPage()
                }
            })
        }
        mAdapter!!.setActionListener(this)
        mAdapter!!.setDataList(mUserList)
        // StickyNotes API
        val baseUrl = requireActivity()
            .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(AppConfig.SHARED_BASE_URL, null)
        mAccessToken = requireActivity()
            .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(AppConfig.SHARED_ACCESS_TOKEN, null)
        if (baseUrl == null) {
            Toast.makeText(context, R.string.text_no_base_url, Toast.LENGTH_SHORT)
                .show()
            return view
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(SimpleResponseDeserializer.TYPE, SimpleResponseDeserializer())
            .registerTypeAdapter(UserListDeserializer.TYPE, UserListDeserializer())
            .registerTypeAdapter(UserListResponseDeserializer.TYPE, UserListResponseDeserializer())
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        mApi = retrofit.create(StickyNotesApi::class.java)
        mSimpleResponseCallback = object : SimpleResponseCallback(context) {
            override fun onResponse(
                call: Call<ServiceResponse<Any>>,
                response: Response<ServiceResponse<Any>>
            ) {
                setWaiting(false)
                super.onResponse(call, response)
                if (response.isSuccessful && !response.body().isError) {
                    refresh()
                }
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    override fun userListNeedActionOne(): Boolean {
        return true
    }

    override fun getActionOneTitle(): String {
        return mActionOneTitle!!
    }

    override fun onUserListActionOne(position: Int) {
        if (mAccessToken == null || mApi == null) {
            return
        }
        val friend = mUserList[position]
        val call = mApi!!.friendUnfriend(mAccessToken, friend.id)
        call.enqueue(mSimpleResponseCallback)
    }

    override fun userListNeedActionTwo(): Boolean {
        return false
    }

    override fun getActionTwoTitle(): String {
        return ""
    }

    override fun onUserListActionTwo(position: Int) {}
    override fun needItemClick(): Boolean {
        return false
    }

    override fun onItemClick(position: Int) {}
    override fun onClick(view: View) {
        if (view.id == R.id.fab) {
            addFriends()
        }
    }

    private fun setWaiting(waiting: Boolean) {
        mPBWait!!.visibility = if (waiting) View.VISIBLE else View.INVISIBLE
        mRVFriends!!.visibility =
            if (waiting) View.INVISIBLE else View.VISIBLE
        if (waiting) {
            mFab!!.hide()
        } else {
            mFab!!.show()
        }
    }

    private fun clearData() {
        mUserList.clear()
        mAdapter!!.notifyDataSetChanged()
    }

    private fun addData(users: List<JsonUser>) {
        mUserList.addAll(users)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun refresh() {
        setWaiting(true)
        clearData()
        mPage = 0
        loadDataPage()
    }

    private fun addFriends() {
        val dialogFragment = FriendSearchDialogFragment()
        dialogFragment.show(childFragmentManager, "FriendSearchDialogFragment")
    }

    private fun loadDataPage() {
        if (mApi == null || mAccessToken == null) {
            return
        }
        val call = mApi!!.friendGetList(mAccessToken, mPage)
        call.enqueue(mUserListCallback)
    }

    private val mUserListCallback: Callback<ServiceResponse<List<JsonUser>>> =
        object : Callback<ServiceResponse<List<JsonUser>>> {
            override fun onResponse(
                call: Call<ServiceResponse<List<JsonUser>>>,
                response: Response<ServiceResponse<List<JsonUser>>>
            ) {
                setWaiting(false)
                if (!response.isSuccessful) {
                    Toast.makeText(
                        activity, response.message()
                                + " (" + response.code().toString() + ")",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val userListResponse = response.body()
                if (userListResponse.isError || !userListResponse.containsData()) {
                    Toast.makeText(activity, userListResponse.message, Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val users = userListResponse.data
                if (users != null) {
                    if (users.size > 0) {
                        mPage++
                        addData(users)
                    }
                }
            }

            override fun onFailure(call: Call<ServiceResponse<List<JsonUser>>>, t: Throwable) {
                Toast.makeText(context, R.string.text_failure, Toast.LENGTH_SHORT)
                    .show()
            }
        }
}