package io.github.alexeychurchill.stickynotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
 * Shared to... dialog fragment
 */

public class SharedToDialogFragment extends DialogFragment implements
        UserListAdapter.OnUserListActionListener {
    private String mUnshareActionTitle;
    private List<JsonUser> mUsers = new ArrayList<>();
    private int mPage = 0;
    private SimpleUserListAdapter mAdapter;
    private StickyNotesApi mApi;
    private String mAccessToken;
    private int mNoteId = -1;
    private SimpleResponseCallback mSimpleResponseCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SimpleUserListAdapter();
        RecyclerView rvUsers = new RecyclerView(getContext());
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(mAdapter);
        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    loadDataPage();
            }
        });
        mAdapter.setActionListener(this);
        mAdapter.setDataList(mUsers);
        // StickyNotes API
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        // Access token
        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        if (baseUrl != null) {
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
        }
        // List title
        mUnshareActionTitle = getString(R.string.text_button_unshare);
        // Simple callback
        mSimpleResponseCallback = new SimpleResponseCallback(getContext()) {
            @Override
            public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() || !response.body().isError()) {
                    refresh();
                }
            }
        };
        loadDataPage();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle(R.string.text_title_shared_to)
                .setView(rvUsers)
                .setNeutralButton(R.string.text_button_cancel, null)
                .create();
    }

    public void setNoteId(int noteId) {
        this.mNoteId = noteId;
    }

    private void clearData() {
        mUsers.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void addData(List<JsonUser> users) {
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        clearData();
        mPage = 0;
        loadDataPage();
    }

    private void loadDataPage() {
        if (mApi == null || mAccessToken == null || mNoteId == -1) {
            return;
        }
        Call<ServiceResponse<List<JsonUser>>> call = mApi.sharedToList(mAccessToken, mNoteId, mPage);
        call.enqueue(mUserListCallback);
    }

    private Callback<ServiceResponse<List<JsonUser>>> mUserListCallback = new Callback<ServiceResponse<List<JsonUser>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<JsonUser>>> call, Response<ServiceResponse<List<JsonUser>>> response) {
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

    @Override
    public boolean userListNeedActionOne() {
        return true;
    }

    @Override
    public String getActionOneTitle() {
        return mUnshareActionTitle;
    }

    @Override
    public void onUserListActionOne(int position) {
        JsonUser user = mUsers.get(position);
        if (mApi == null || mAccessToken == null || mNoteId == -1) {
            return;
        }
        Call<ServiceResponse<Object>> call = mApi.sharedUnshare(mAccessToken, mNoteId, user.getId());
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
}
