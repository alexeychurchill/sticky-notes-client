package io.github.alexeychurchill.stickynotes.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.fragment.LoginFragment;
import io.github.alexeychurchill.stickynotes.fragment.RegisterFragment;
import io.github.alexeychurchill.stickynotes.listener.OnLoggedInListener;

/**
 * Login and register activity
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        OnLoggedInListener {
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;
    private TextView mTVButtonActionTitle;
    private Action mAction = Action.LOGIN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit()
                .putString(AppConfig.SHARED_BASE_URL, AppConfig.BASE_URL) // TODO: 26.12.2016 Delete
                .apply();
        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();
        // Initial fragment
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.flFragmentContainer, mLoginFragment)
                .commit();
        // Swap fragments button
        mTVButtonActionTitle = ((TextView) findViewById(R.id.tvButtonActionTitle));
        LinearLayout llButtonSwapAction = ((LinearLayout) findViewById(R.id.llButtonSwapAction));
        if (llButtonSwapAction != null) {
            llButtonSwapAction.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llButtonSwapAction:
                swapAction();
                break;
        }
    }

    private void swapAction() {
        switch (mAction) {
            case LOGIN:
                switchActionRegister();
                break;
            case REGISTER:
                switchActionLogin();
                break;
        }
    }

    private void switchActionLogin() {
        mAction = Action.LOGIN;
        switchFragment(mLoginFragment);
        mTVButtonActionTitle.setText(R.string.text_button_join);
    }

    private void switchActionRegister() {
        mAction = Action.REGISTER;
        switchFragment(mRegisterFragment);
        mTVButtonActionTitle.setText(R.string.text_button_back_login);
    }


    private void switchFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onLoggedId(String accessToken) {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private enum Action {
        LOGIN,
        REGISTER;
    }
}
