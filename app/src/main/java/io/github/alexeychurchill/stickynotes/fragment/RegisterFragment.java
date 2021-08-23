package io.github.alexeychurchill.stickynotes.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Register fragment
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText mETLogin;
    private EditText mETPassword;
    private Button mBtnRegister;
    private ProgressBar mPBWait;
    private StickyNotesApi mApi;
    private SimpleResponseCallback mSimpleResponseCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mETLogin = ((EditText) view.findViewById(R.id.etLogin));
        mETPassword = ((EditText) view.findViewById(R.id.etPassword));
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        mBtnRegister = ((Button) view.findViewById(R.id.btnRegister));
        if (mBtnRegister != null) {
            mBtnRegister.setOnClickListener(this);
        }
        // Setting retrofit
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, "");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);
        // Simple response callback
        mSimpleResponseCallback = new SimpleResponseCallback(getContext()) {
            @Override
            public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
                setWaiting(false);
                super.onResponse(call, response);
            }
        };
        return view;
    }

    private void setWaiting(boolean waiting) {
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mBtnRegister.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegister) {
            callRegister();
        }
    }

    private void callRegister() {
        String login = mETLogin.getText().toString();
        String password = mETPassword.getText().toString();
        if (login.contains(" ")) {
            Toast.makeText(getContext(), R.string.text_login_contains_space, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), R.string.text_login_or_password_is_empty, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        setWaiting(true);
        Call<ServiceResponse<Object>> call = mApi.userRegister(login, password);
        call.enqueue(mSimpleResponseCallback);
    }
}
