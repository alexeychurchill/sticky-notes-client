package io.github.alexeychurchill.stickynotes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import io.github.alexeychurchill.stickynotes.listener.EndlessRecyclerViewScrollListener;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.User;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.UserListResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dialog which allows to share note to user
 */

public class ShareNoteDialogFragment extends DialogFragment implements UserListAdapter.OnUserListActionListener, AllowEditDialogFragment.OnAllowDecisionListener {
    private StickyNotesApi mApi;
    private String mAccessToken;
    private SimpleResponseCallback mSimpleResponseCallback;
    private SimpleUserListAdapter mAdapter;
    private List<User> mUsers = new ArrayList<>();
    private int mPage = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Access token
        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        // Api
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserListResponseDeserializer.TYPE, new UserListResponseDeserializer())
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .create();
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        if (baseUrl != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mApi = retrofit.create(StickyNotesApi.class);
        }
        mSimpleResponseCallback = new SimpleResponseCallback(getContext());
        // RecyclerView
        mAdapter = new SimpleUserListAdapter();
        mAdapter.setDataList(mUsers);
        mAdapter.setActionListener(this);
        RecyclerView rvUsers = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(mAdapter);
        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadDataPage();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        loadDataPage();
        return builder
                .setTitle(R.string.text_title_share_to)
                .setView(rvUsers)
                .setNeutralButton(R.string.text_button_cancel, null)
                .create();
    }

    private void addData(List<User> users) {
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    private void loadDataPage() {
        if (mApi == null || mAccessToken == null) {
            return;
        }
        Call<ServiceResponse<List<User>>> call = mApi.friendGetList(mAccessToken, mPage);
        call.enqueue(mUserListCallback);
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
        return true;
    }

    @Override
    public void onItemClick(int position) {
        AllowEditDialogFragment dialog = new AllowEditDialogFragment();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), "AllowEditDialogFragment");
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

    @Override
    public void onAllowEditDecision(boolean allowed) {
//        Call<ServiceResponse<Object>> call = mApi.sharedShare(mAccessToken, )
    }
}
