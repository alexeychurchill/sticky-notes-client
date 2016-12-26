package io.github.alexeychurchill.stickynotes.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.model.LoginResult;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.deserializer.LoginResultDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Login fragment
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private EditText mETLogin;
    private EditText mETPassword;
    private ProgressBar mPBWait;
    private Button mBtnLogin;
    private StickyNotesApi mApi;
    private OnLoggedInListener mLoggedInListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Setting views
        mETLogin = ((EditText) view.findViewById(R.id.etLogin));
        mETPassword = ((EditText) view.findViewById(R.id.etPassword));
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        mBtnLogin = ((Button) view.findViewById(R.id.btnLogin));
        if (mBtnLogin != null) {
            mBtnLogin.setOnClickListener(this);
        }
        // Setting retrofit
        String baseUrl = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, "");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .registerTypeAdapter(LoginResultDeserializer.TYPE, new LoginResultDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                callLogin();
                break;
        }
    }

    public void setLoggedInListener(OnLoggedInListener loggedInListener) {
        this.mLoggedInListener = loggedInListener;
    }

    private void callLogin() {
        String login = mETLogin.getText().toString();
        String password = mETPassword.getText().toString();
        setWaiting(true);
        Call<ServiceResponse<LoginResult>> loginCall = mApi.userLogin(login, password);
        loginCall.enqueue(loginCallback);
    }

    private void setWaiting(boolean waiting) {
        if ((mPBWait == null) || (mBtnLogin == null)) {
            return;
        }
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mBtnLogin.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

    private Callback<ServiceResponse<LoginResult>> loginCallback = new Callback<ServiceResponse<LoginResult>>() {
        @Override
        public void onResponse(Call<ServiceResponse<LoginResult>> call, Response<ServiceResponse<LoginResult>> response) {
            setWaiting(false);
            if (!response.isSuccessful()) {
                Toast.makeText(getContext(),
                        response.message()
                        .concat(" (")
                        .concat(String.valueOf(response.code()))
                        .concat(")"),
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            ServiceResponse<LoginResult> loginResultResponse = response.body();
            if (!loginResultResponse.containsData() && loginResultResponse.isError()) {
                Toast.makeText(getContext(), loginResultResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            LoginResult loginResult = loginResultResponse.getData();
            getActivity().getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putString(AppConfig.SHARED_ACCESS_TOKEN, loginResult.getAccessToken())
                    .apply();
            Toast.makeText(getContext(), R.string.text_logged_in, Toast.LENGTH_LONG)
                    .show();
            if (mLoggedInListener != null) {
                mLoggedInListener.onLoggedId(loginResult.getAccessToken());
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<LoginResult>> call, Throwable t) {
            Toast.makeText(getContext(), R.string.text_failure, Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "onFailure: t: " + t);
            setWaiting(false);
        }
    };
}
