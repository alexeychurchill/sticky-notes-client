package io.github.alexeychurchill.stickynotes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Friend search dialog fragment
 */

public class FriendSearchDialogFragment extends DialogFragment implements
        UserListAdapter.OnUserListActionListener {

    private StickyNotesApi mApi;
    private String mAccessToken;
    private Button mBtnSearch;
    private EditText mETQuery;
    private ProgressBar mPBWait;
    private SimpleResponseCallback mSimpleResponseCallback;
    private SimpleUserListAdapter mAdapter;
    private List<User> mUsers = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_friend_request, null, false);
        mBtnSearch = ((Button) view.findViewById(R.id.btnSearch));
        mETQuery = ((EditText) view.findViewById(R.id.etQuery));
        if (mBtnSearch != null) {
            mBtnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search(mETQuery.getText().toString());
                }
            });
        }
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
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
        // RecyclerView
        mAdapter = new SimpleUserListAdapter();
        mAdapter.setDataList(mUsers);
        mAdapter.setActionListener(this);
        RecyclerView rvUsers = ((RecyclerView) view.findViewById(R.id.rvUsers));
        if (rvUsers != null) {
            rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
            rvUsers.setAdapter(mAdapter);
        }
        // Simple response callback
        mSimpleResponseCallback = new SimpleResponseCallback(getContext()) {
            @Override
            public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
                setWaiting(false);
                super.onResponse(call, response);
                if (response.isSuccessful() && !response.body().isError()) {
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<Object>> call, Throwable t) {
                setWaiting(false);
                super.onFailure(call, t);
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle(R.string.text_title_search_users)
                .setView(view)
                .setNeutralButton(R.string.text_button_cancel, null)
                .create();
    }

    private void search(String query) {
        if (mApi == null || mAccessToken == null) {
            return;
        }
        Call<ServiceResponse<List<User>>> call = mApi.userSearch(mAccessToken, query);
        call.enqueue(userListCallback);
    }

    private void setWaiting(boolean waiting) {
        if (mBtnSearch == null || mPBWait == null) {
            return;
        }
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mBtnSearch.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

    private Callback<ServiceResponse<List<User>>> userListCallback = new Callback<ServiceResponse<List<User>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<User>>> call, Response<ServiceResponse<List<User>>> response) {
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
            ServiceResponse<List<User>> userListResponse = response.body();
            if (userListResponse.isError() || !userListResponse.containsData()) {
                Toast.makeText(getActivity(), userListResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            List<User> users = userListResponse.getData();
            mUsers.clear();
            mAdapter.notifyDataSetChanged();
            if (users != null) {
                mUsers.addAll(users);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<List<User>>> call, Throwable t) {
            Toast.makeText(getContext(), "" + t, Toast.LENGTH_LONG)
                    .show();
        }
    };

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
        if (mAccessToken == null || mApi == null) {
            return;
        }
        setWaiting(true);
        User user = mUsers.get(position);
        Call<ServiceResponse<Object>> call = mApi.friendRequest(mAccessToken, user.getId());
        call.enqueue(mSimpleResponseCallback);
    }
}
