package io.github.alexeychurchill.stickynotes.fragment.users;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.adapter.FriendRequestUserListAdapter;
import io.github.alexeychurchill.stickynotes.adapter.UserListAdapter;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback;
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener;
import io.github.alexeychurchill.stickynotes.model.FriendRequest;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.deserializer.FriendRequestListDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.FriendRequestListResponseDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Friend request list fragment
 */

public abstract class FriendRequestListFragment extends Fragment implements
        UserListAdapter.OnUserListActionListener {
    private StickyNotesApi mApi;
    private SimpleResponseCallback mSimpleResponseCallback;
    private int mPage = 0;
    private List<FriendRequest> mFriendRequests = new LinkedList<>();
    private String mAccessToken;

    private ProgressBar mPBWait;
    private RecyclerView mRVFriendRequests;
    private FriendRequestUserListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request_list, container, false);
        // Wait progress bar
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        // List
        mAdapter = new FriendRequestUserListAdapter();
        mAdapter.setDataList(mFriendRequests);
        mAdapter.setActionListener(this);
        mRVFriendRequests = ((RecyclerView) view.findViewById(R.id.rvFriendRequests));
        if (mRVFriendRequests != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRVFriendRequests.setLayoutManager(layoutManager);
            mRVFriendRequests.setAdapter(mAdapter);
            mRVFriendRequests.addOnScrollListener(
                    new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    loadDataPage();
                }
            });
        }
        // Access token
        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        // StickyNotes API
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        if (baseUrl == null) {
            Toast.makeText(getContext(), R.string.text_no_base_url, Toast.LENGTH_SHORT)
                    .show();
            return view;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .registerTypeAdapter(FriendRequestListDeserializer.TYPE, new FriendRequestListDeserializer())
                .registerTypeAdapter(
                        FriendRequestListResponseDeserializer.TYPE,
                        new FriendRequestListResponseDeserializer()
                ).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);
        mSimpleResponseCallback = new SimpleResponseCallback(getContext()) {
            @Override
            public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
                setWaiting(false);
                super.onResponse(call, response);
                if (response.isSuccessful() && !response.body().isError()) {
                    refresh();
                }
            }
        };
        onInit(view);
        return view;
    }

    protected void onInit(View view) {
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    public void refresh() {
        setWaiting(true);
        clearData();
        loadDataPage();
    }

    @Override
    public boolean userListNeedActionOne() {
        return false;
    }

    @Override
    public String getActionOneTitle() {
        return null;
    }

    @Override
    public void onUserListActionOne(int position) {
        Call<ServiceResponse<Object>> call = getActionOneCall(mFriendRequests.get(position));
        if (call == null) {
            return;
        }
        setWaiting(true);
        call.enqueue(mSimpleResponseCallback);
    }

    protected Call<ServiceResponse<Object>> getActionOneCall(FriendRequest friendRequest) {
        return null;
    }

    @Override
    public boolean userListNeedActionTwo() {
        return false;
    }

    @Override
    public String getActionTwoTitle() {
        return null;
    }

    @Override
    public void onUserListActionTwo(int position) {
        Call<ServiceResponse<Object>> call = getActionTwoCall(mFriendRequests.get(position));
        if (call == null) {
            return;
        }
        setWaiting(true);
        call.enqueue(mSimpleResponseCallback);
    }

    protected Call<ServiceResponse<Object>> getActionTwoCall(FriendRequest friendRequest) {
        return null;
    }

    @Override
    public boolean needItemClick() {
        return false;
    }

    @Override
    public void onItemClick(int position) {

    }

    protected void setWaiting(boolean waiting) {
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mRVFriendRequests.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

    protected void clearData() {
        mFriendRequests.clear();
        mPage = 0;
        mAdapter.notifyDataSetChanged();
    }

    protected void addData(List<FriendRequest> requests) {
        if (requests == null) {
            return;
        }
        mFriendRequests.addAll(requests);
        mAdapter.notifyDataSetChanged();
    }

    protected void loadDataPage() {
        Call<ServiceResponse<List<FriendRequest>>> friendRequestCall = getLoadDataPageCall(mPage);
        if (friendRequestCall == null) {
            return;
        }
        friendRequestCall.enqueue(mFriendRequestListCallback);
    }

    protected StickyNotesApi getApi() {
        return mApi;
    }

    protected String getAccessToken() {
        return mAccessToken;
    }

    protected abstract Call<ServiceResponse<List<FriendRequest>>> getLoadDataPageCall(int page);

    private Callback<ServiceResponse<List<FriendRequest>>> mFriendRequestListCallback =
            new Callback<ServiceResponse<List<FriendRequest>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<FriendRequest>>> call, Response<ServiceResponse<List<FriendRequest>>> response) {
            setWaiting(false);
            if (!response.isSuccessful()) {
                Toast.makeText(
                        getActivity(),
                        response.message()
                                .concat(" (")
                                .concat(String.valueOf(response.code()))
                                .concat(")"),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            ServiceResponse<List<FriendRequest>> requestListResponse = response.body();
            if (requestListResponse.isError() && !requestListResponse.containsData()) {
                Toast.makeText(getActivity(), requestListResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            List<FriendRequest> friendRequests = requestListResponse.getData();
            if (friendRequests != null) {
                if (friendRequests.size() > 0) {
                    addData(friendRequests);
                    mPage++;
                }
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<List<FriendRequest>>> call, Throwable t) {
            setWaiting(false);
            Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
