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
import com.google.gson.GsonBuilder
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.adapter.FriendRequestUserListAdapter
import io.github.alexeychurchill.stickynotes.adapter.UserListAdapter.OnUserListActionListener
import io.github.alexeychurchill.stickynotes.api.AppConfig
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener
import io.github.alexeychurchill.stickynotes.model.FriendRequest
import io.github.alexeychurchill.stickynotes.model.ServiceResponse
import io.github.alexeychurchill.stickynotes.model.deserializer.FriendRequestListDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.FriendRequestListResponseDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Friend request list fragment
 */
abstract class FriendRequestListFragment : Fragment(), OnUserListActionListener {
    protected var api: StickyNotesApi? = null
        private set
    private var mSimpleResponseCallback: SimpleResponseCallback? = null
    private var mPage = 0
    private val mFriendRequests: MutableList<FriendRequest> = LinkedList()
    protected var accessToken: String? = null
        private set
    private var mPBWait: ProgressBar? = null
    private var mRVFriendRequests: RecyclerView? = null
    private var mAdapter: FriendRequestUserListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_request_list, container, false)
        // Wait progress bar
        mPBWait = view.findViewById<View>(R.id.pbWait) as ProgressBar
        // List
        mAdapter = FriendRequestUserListAdapter()
        mAdapter!!.setDataList(mFriendRequests)
        mAdapter!!.setActionListener(this)
        mRVFriendRequests = view.findViewById<View>(R.id.rvFriendRequests) as RecyclerView
        if (mRVFriendRequests != null) {
            val layoutManager = LinearLayoutManager(context)
            mRVFriendRequests!!.layoutManager = layoutManager
            mRVFriendRequests!!.adapter = mAdapter
            mRVFriendRequests!!.addOnScrollListener(
                object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        loadDataPage()
                    }
                })
        }
        // Access token
        accessToken = requireActivity()
            .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(AppConfig.SHARED_ACCESS_TOKEN, null)
        // StickyNotes API
        val baseUrl = requireActivity()
            .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(AppConfig.SHARED_BASE_URL, null)
        if (baseUrl == null) {
            Toast.makeText(context, R.string.text_no_base_url, Toast.LENGTH_SHORT)
                .show()
            return view
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(SimpleResponseDeserializer.TYPE, SimpleResponseDeserializer())
            .registerTypeAdapter(
                FriendRequestListDeserializer.TYPE,
                FriendRequestListDeserializer()
            )
            .registerTypeAdapter(
                FriendRequestListResponseDeserializer.TYPE,
                FriendRequestListResponseDeserializer()
            ).create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(StickyNotesApi::class.java)
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
        onInit(view)
        return view
    }

    protected open fun onInit(view: View?) {}
    override fun onStart() {
        super.onStart()
        refresh()
    }

    fun refresh() {
        setWaiting(true)
        clearData()
        loadDataPage()
    }

    override fun userListNeedActionOne(): Boolean {
        return false
    }

    override fun getActionOneTitle(): String {
        return ""
    }

    override fun onUserListActionOne(position: Int) {
        val call = getActionOneCall(mFriendRequests[position]) ?: return
        setWaiting(true)
        call.enqueue(mSimpleResponseCallback)
    }

    protected open fun getActionOneCall(friendRequest: FriendRequest?): Call<ServiceResponse<Any>>? {
        return null
    }

    override fun userListNeedActionTwo(): Boolean {
        return false
    }

    override fun getActionTwoTitle(): String {
        return ""
    }

    override fun onUserListActionTwo(position: Int) {
        val call = getActionTwoCall(mFriendRequests[position]) ?: return
        setWaiting(true)
        call.enqueue(mSimpleResponseCallback)
    }

    protected open fun getActionTwoCall(friendRequest: FriendRequest?): Call<ServiceResponse<Any>>? {
        return null
    }

    override fun needItemClick(): Boolean {
        return false
    }

    override fun onItemClick(position: Int) {}
    protected fun setWaiting(waiting: Boolean) {
        mPBWait!!.visibility = if (waiting) View.VISIBLE else View.INVISIBLE
        mRVFriendRequests!!.visibility = if (waiting) View.INVISIBLE else View.VISIBLE
    }

    protected fun clearData() {
        mFriendRequests.clear()
        mPage = 0
        mAdapter!!.notifyDataSetChanged()
    }

    protected fun addData(requests: List<FriendRequest>?) {
        if (requests == null) {
            return
        }
        mFriendRequests.addAll(requests)
        mAdapter!!.notifyDataSetChanged()
    }

    protected fun loadDataPage() {
        val friendRequestCall = getLoadDataPageCall(mPage) ?: return
        friendRequestCall.enqueue(mFriendRequestListCallback)
    }

    protected abstract fun getLoadDataPageCall(page: Int): Call<ServiceResponse<List<FriendRequest>>>?
    private val mFriendRequestListCallback: Callback<ServiceResponse<List<FriendRequest>>> =
        object : Callback<ServiceResponse<List<FriendRequest>>> {
            override fun onResponse(
                call: Call<ServiceResponse<List<FriendRequest>>>,
                response: Response<ServiceResponse<List<FriendRequest>>>
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
                val requestListResponse = response.body()
                if (requestListResponse.isError && !requestListResponse.containsData()) {
                    Toast.makeText(activity, requestListResponse.message, Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val friendRequests = requestListResponse.data
                if (friendRequests != null) {
                    if (friendRequests.size > 0) {
                        addData(friendRequests)
                        mPage++
                    }
                }
            }

            override fun onFailure(
                call: Call<ServiceResponse<List<FriendRequest>>>,
                t: Throwable
            ) {
                setWaiting(false)
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
}