package io.github.alexeychurchill.stickynotes.activity.main.friends.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.activity.main.friends.adapter.SimpleUserListAdapter;
import io.github.alexeychurchill.stickynotes.activity.main.friends.adapter.UserListAdapter;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.User;
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
    private List<User> mUserList = new ArrayList<>();
    private int mPage = 0;
    private SimpleUserListAdapter mAdapter;

    private StickyNotesApi mApi;
    private String mAccessToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        // Action title
        mActionOneTitle = getString(R.string.text_button_delete);
        // FAB
        FloatingActionButton fab = ((FloatingActionButton) view.findViewById(R.id.fab));
        if (fab != null) {
            fab.setOnClickListener(this);
        }
        // RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SimpleUserListAdapter();
        RecyclerView rvFriends = ((RecyclerView) view.findViewById(R.id.rvFriends));
        if (rvFriends != null) {
            rvFriends.setLayoutManager(layoutManager);
            rvFriends.setAdapter(mAdapter);
            rvFriends.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    callLoadData();
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
                .registerTypeAdapter(UserListDeserializer.TYPE, new UserListDeserializer())
                .registerTypeAdapter(UserListResponseDeserializer.TYPE, new UserListResponseDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);

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
            callAddFriends();
        }
    }

    private void clearData() {
        mUserList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void addData(List<User> users) {
        mUserList.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        clearData();
        mPage = 0;
        callLoadData();
    }

    private void callAddFriends() {
        //...
    }

    private void callLoadData() {
        if (mAccessToken == null) {
            Toast.makeText(getContext(), R.string.text_access_token_is_null, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Call<ServiceResponse<List<User>>> call = mApi.friendGetList(mAccessToken, mPage);
        call.enqueue(mUserListCallback);
    }

    private Callback<ServiceResponse<List<User>>> mUserListCallback = new Callback<ServiceResponse<List<User>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<User>>> call, Response<ServiceResponse<List<User>>> response) {
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
            ServiceResponse<List<User>> userListResponse = response.body();
            if (userListResponse.isError() || !userListResponse.containsData()) {
                Toast.makeText(getActivity(), userListResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            List<User> users = userListResponse.getData();
            if (users != null) {
                if (users.size() > 0) {
                    mPage++;
                    addData(users);
                }
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<List<User>>> call, Throwable t) {
            Toast.makeText(getContext(), R.string.text_failure, Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
