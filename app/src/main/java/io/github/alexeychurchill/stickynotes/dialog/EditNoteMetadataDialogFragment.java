package io.github.alexeychurchill.stickynotes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dialog for editing metadata
 */

public class EditNoteMetadataDialogFragment extends DialogFragment {
    private int mNoteId = -1;
    private StickyNotesApi mApi;
    private String mAccessToken;
    private LinearLayout mLLControls;
    private ProgressBar mPBWait;
    private EditText mETTitle;
    private String mNoteSourceTitle;
    private EditText mETSubject;
    private String mNoteSourceSubject;
    private SimpleResponseCallback mSimpleResponseCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // View
        View view = LayoutInflater
                .from(getContext()).inflate(R.layout.dialog_edit_note_metadata, null, false);
        // Views
        mLLControls = ((LinearLayout) view.findViewById(R.id.llControls));
        mETTitle = ((EditText) view.findViewById(R.id.etTitle));
        mETSubject = ((EditText) view.findViewById(R.id.etSubject));
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        // Initialization
        mETTitle.setText(mNoteSourceTitle);
        mETSubject.setText((mNoteSourceSubject != null) ? mNoteSourceSubject : "");
        // Api
        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        if (baseUrl != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mApi = retrofit.create(StickyNotesApi.class);
        }
        mSimpleResponseCallback = new SimpleResponseCallback(getContext()) {
            @Override
            public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
                setWaiting(false);
                super.onResponse(call, response);
                if (response.isSuccessful() && !response.body().isError()) {
                    dismiss();
                }
            }
        };
        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder
                .setTitle(R.string.text_title_update_metadata)
                .setView(view)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_update, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialog alertDialog = ((AlertDialog) dialogInterface);
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editNoteMetadata();
                    }
                });
            }
        });
        return dialog;
    }

    public void setNote(NoteEntry note) {
        mNoteId = note.getId();
        mNoteSourceTitle = note.getTitle();
        mNoteSourceSubject = note.getSubject();
    }

    private void editNoteMetadata() {
        if (mAccessToken == null || mApi == null || mNoteId == -1) {
            return;
        }
        String title = mETTitle.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), R.string.text_title_cant_be_empty, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String subject = mETSubject.getText().toString();
        setWaiting(true);
        Call<ServiceResponse<Object>> call = mApi.noteUpdateMetadata(mAccessToken, mNoteId, title, subject);
        call.enqueue(mSimpleResponseCallback);
    }

    private void setWaiting(boolean waiting) {
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mLLControls.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

}
