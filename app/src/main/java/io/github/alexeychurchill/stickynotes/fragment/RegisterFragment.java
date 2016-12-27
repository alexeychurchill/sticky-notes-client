package io.github.alexeychurchill.stickynotes.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import io.github.alexeychurchill.stickynotes.R;

/**
 * Register fragment
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText mETLogin;
    private EditText mETPassword;
    private ProgressBar mPBWait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mETLogin = ((EditText) view.findViewById(R.id.etLogin));
        mETPassword = ((EditText) view.findViewById(R.id.etPassword));
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        Button btnRegister = ((Button) view.findViewById(R.id.btnRegister));
        if (btnRegister != null) {
            btnRegister.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        //...
    }
}
