package io.github.alexeychurchill.stickynotes.fragment.users;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.adapter.SimpleUserListAdapter;
import io.github.alexeychurchill.stickynotes.adapter.UserListAdapter;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback;
import io.github.alexeychurchill.stickynotes.dialog.FriendSearchDialogFragment;
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.JsonUser;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.UserListDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.UserListResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Base friends list fragment
 */

public class FriendsListFragment extends Fragment implements
        UserListAdapter.OnUserListActionListener, View.OnClickListener {
    private String mActionOneTitle;
    private List<JsonUser> mUserList = new ArrayList<>();
    private int mPage = 0;
    private SimpleUserListAdapter mAdapter;
    private SimpleResponseCallback mSimpleResponseCallback;

    private ProgressBar mPBWait;
    private RecyclerView mRVFriends;
    private FloatingActionButton mFab;

    private StickyNotesApi mApi;
    private String mAccessToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        // Action title
        mActionOneTitle = getString(R.string.text_button_delete);
        // FAB
        mFab = ((FloatingActionButton) view.findViewById(R.id.fab));
        if (mFab != null) {
            mFab.setOnClickListener(this);
        }
        // RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SimpleUserListAdapter();
        mRVFriends = ((RecyclerView) view.findViewById(R.id.rvFriends));
        if (mRVFriends != null) {
            mRVFriends.setLayoutManager(layoutManager);
            mRVFriends.setAdapter(mAdapter);
            mRVFriends.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    loadDataPage();
                }
            });
        }
        mAdapter.setActionListener(this);
        mAdapter.setDataList(mUserList);
        // StickyNotes API
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);

        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);

        if (baseUrl == null) {
            Toast.makeText(getContext(), R.string.text_no_base_url, Toast.LENGTH_SHORT)
                    .show();
            return view;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .registerTypeAdapter(UserListDeserializer.TYPE, new UserListDeserializer())
                .registerTypeAdapter(UserListResponseDeserializer.TYPE, new UserListResponseDeserializer())
                .create();
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public boolean userListNeedActionOne() {
        return true;
    }

    @Override
    public String getActionOneTitle() {
        return mActionOneTitle;
    }

    @Override
    public void onUserListActionOne(int position) {
        if (mAccessToken == null || mApi == null) {
            return;
        }
        JsonUser friend = mUserList.get(position);
        Call<ServiceResponse<Object>> call = mApi.friendUnfriend(mAccessToken, friend.getId());
        call.enqueue(mSimpleResponseCallback);
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
    }

    @Override
    public boolean needItemClick() {
        return false;
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            addFriends();
        }
    }

    private void setWaiting(boolean waiting) {
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mRVFriends.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
        if (waiting) {
            mFab.hide();
        } else {
            mFab.show();
        }
    }

    private void clearData() {
        mUserList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void addData(List<JsonUser> users) {
        mUserList.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        setWaiting(true);
        clearData();
        mPage = 0;
        loadDataPage();
    }

    private void addFriends() {
        FriendSearchDialogFragment dialogFragment = new FriendSearchDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "FriendSearchDialogFragment");
    }

    private void loadDataPage() {
        if (mApi == null || mAccessToken == null) {
            return;
        }
        Call<ServiceResponse<List<JsonUser>>> call = mApi.friendGetList(mAccessToken, mPage);
        call.enqueue(mUserListCallback);
    }

    private Callback<ServiceResponse<List<JsonUser>>> mUserListCallback = new Callback<ServiceResponse<List<JsonUser>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<JsonUser>>> call, Response<ServiceResponse<List<JsonUser>>> response) {
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
            ServiceResponse<List<JsonUser>> userListResponse = response.body();
            if (userListResponse.isError() || !userListResponse.containsData()) {
                Toast.makeText(getActivity(), userListResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            List<JsonUser> users = userListResponse.getData();
            if (users != null) {
                if (users.size() > 0) {
                    mPage++;
                    addData(users);
                }
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<List<JsonUser>>> call, Throwable t) {
            Toast.makeText(getContext(), R.string.text_failure, Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
