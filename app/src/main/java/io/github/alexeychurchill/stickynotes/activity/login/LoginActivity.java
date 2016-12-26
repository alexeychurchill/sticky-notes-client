package io.github.alexeychurchill.stickynotes.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.activity.main.MainActivity;
import io.github.alexeychurchill.stickynotes.api.AppConfig;

/**
 * Login and register activity
 */

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
        mLoginFragment.setLoggedInListener(this);
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
