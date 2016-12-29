package io.github.alexeychurchill.stickynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback;
import io.github.alexeychurchill.stickynotes.dialog.ShareNoteDialogFragment;
import io.github.alexeychurchill.stickynotes.model.NoteFull;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.SharedNoteFull;
import io.github.alexeychurchill.stickynotes.model.deserializer.NoteFullResponseDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.SharedNoteResponseDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Note view and edit activity
 */

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_NOTE_ID = AppConfig.APP_PACKAGE.concat(".EXTRA_NOTE_ID");
    public static final String EXTRA_NOTE_SHARED = AppConfig.APP_PACKAGE.concat(".EXTRA_NOTE_SHARED");

    private StickyNotesApi mApi;

    private boolean mHasNote = false;
    private NoteFull mNote;
    private int mNoteId = -1;
    private boolean mEditable = true;
    private boolean mShared = false;

    private String mAccessToken;
    private EditText mETNoteText;
    private ProgressBar mPBWait;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        // Toolbar
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Views
        initViews();
        // Initialize with data
        if (!initData(getIntent())) {
            finish();
            return;
        }
        // API
        mSimpleResponseCallback = new SimpleResponseCallback(this);
        // Base URL
        String baseUrl = getSharedPreferences(AppConfig.APP_PREFERENCES, MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        if (baseUrl == null) {
            Toast.makeText(this, R.string.text_no_base_url, Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }
        // Access token
        mAccessToken = getSharedPreferences(AppConfig.APP_PREFERENCES, MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        if (mAccessToken == null) {
            Toast.makeText(this, R.string.text_access_token_is_null, Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SharedNoteResponseDeserializer.TYPE, new SharedNoteResponseDeserializer())
                .registerTypeAdapter(NoteFullResponseDeserializer.TYPE, new NoteFullResponseDeserializer())
                .registerTypeAdapter(SimpleResponseDeserializer.TYPE, new SimpleResponseDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);
        callLoadNote();
    }

    private boolean initData(Intent intent) {
        // Note id
        if (!intent.hasExtra(EXTRA_NOTE_ID)) {
            Toast.makeText(this, R.string.text_no_note_id, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        mNoteId = intent.getIntExtra(EXTRA_NOTE_ID, mNoteId);
        // Is shared
        if (!intent.hasExtra(EXTRA_NOTE_SHARED)) {
            Toast.makeText(this, R.string.text_no_shared_flag, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        mShared = intent.getBooleanExtra(EXTRA_NOTE_SHARED, mShared);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mShared) {
            menuSwitchShared(menu);
        } else {
            menuSwitchOwn(menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShareNote) {
            shareThisNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareThisNote() {
        ShareNoteDialogFragment dialog = new ShareNoteDialogFragment();
        dialog.setNoteId(mNoteId);
        dialog.show(getSupportFragmentManager(), "ShareNoteDialogFragment");
    }

    private void menuSwitchShared(Menu menu) {
        for (int menuNumber = 0; menuNumber < menu.size(); menuNumber++) {
            MenuItem menuItem = menu.getItem(menuNumber);
            switch (menuItem.getItemId()) {
                case R.id.menuSharedTo:
                case R.id.menuShareNote:
                case R.id.menuEditMetadata:
                    menuItem.setVisible(false);
                    break;
            }
        }
    }

    private void menuSwitchOwn(Menu menu) {
        for (int menuNumber = 0; menuNumber < menu.size(); menuNumber++) {
            MenuItem menuItem = menu.getItem(menuNumber);
            switch (menuItem.getItemId()) {
                case R.id.menuEditMetadata:
                case R.id.menuSharedTo:
                case R.id.menuShareNote:
                    menuItem.setVisible(true);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            callUpdate();
        }
    }

    private void initViews() {
        mETNoteText = ((EditText) findViewById(R.id.etNoteText));
        mPBWait = ((ProgressBar) findViewById(R.id.pbWait));
        mFab = ((FloatingActionButton) findViewById(R.id.fab));
        mFab.setOnClickListener(this);
    }

    private void setNote(NoteFull note) {
        mNote = note;
        mEditable = true;
        mHasNote = true;
        mETNoteText.setText(note.getText());
        mETNoteText.setClickable(true);
        mETNoteText.setFocusable(true);
        mFab.show();
    }

    private void setNote(SharedNoteFull note) {
        mNote = note.getNote();
        mEditable = note.canEdit();
        mHasNote = true;
        mETNoteText.setText(mNote.getText());
        mETNoteText.setClickable(mEditable);
        mETNoteText.setFocusable(mEditable);
        if (mEditable) {
            mFab.show();
        } else {
            mFab.hide();
        }
    }

    private void setWaiting(boolean waiting) {
        if (waiting) {
            mFab.hide();
        } else {
            mFab.show();
        }
        mETNoteText.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
    }

    private void callLoadNote() {
        if (mNoteId == -1 || mAccessToken == null) {
            return;
        }
        setWaiting(true);
        if (mShared) {
            Call<ServiceResponse<SharedNoteFull>> call = mApi.sharedGet(mAccessToken, mNoteId);
            call.enqueue(mSharedNoteCallback);
        } else { // users note call
            Call<ServiceResponse<NoteFull>> call = mApi.noteGet(mAccessToken, mNoteId);
            call.enqueue(mNoteCallback);
        }
    }

    private void callUpdate() {
        if (!mHasNote || mAccessToken == null) {
            return;
        }
        if (mShared) {
            callSharedNoteUpdate();
        } else {
            callUserNoteUpdate();
        }
    }

    private void callSharedNoteUpdate() {
        if (!mEditable) {
            return;
        }
        String newText = mETNoteText.getText().toString();
        Call<ServiceResponse<Object>> call = mApi.sharedUpdate(mAccessToken, mNote.getId(), newText);
        call.enqueue(mSimpleResponseCallback);
    }

    private void callUserNoteUpdate() {
        String newText = mETNoteText.getText().toString();
        Call<ServiceResponse<Object>> call = mApi.noteUpdate(mAccessToken, mNote.getId(), newText);
        call.enqueue(mSimpleResponseCallback);
    }

    private SimpleResponseCallback mSimpleResponseCallback;

    private Callback<ServiceResponse<NoteFull>> mNoteCallback = new Callback<ServiceResponse<NoteFull>>() {
        @Override
        public void onResponse(Call<ServiceResponse<NoteFull>> call, Response<ServiceResponse<NoteFull>> response) {
            setWaiting(false);
            if (!response.isSuccessful()) {
                Toast.makeText(
                        NoteActivity.this,
                        response.message()
                                .concat(" (")
                                .concat(String.valueOf(response.code()))
                                .concat(")"),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            ServiceResponse<NoteFull> noteFullResponse = response.body();
            if (noteFullResponse.isError() && !noteFullResponse.containsData()) {
                Toast.makeText(NoteActivity.this, noteFullResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            NoteFull note = noteFullResponse.getData();
            setNote(note);
        }

        @Override
        public void onFailure(Call<ServiceResponse<NoteFull>> call, Throwable t) {
            Toast.makeText(NoteActivity.this, R.string.text_failure, Toast.LENGTH_SHORT)
                    .show();
        }
    };

    private Callback<ServiceResponse<SharedNoteFull>> mSharedNoteCallback = new Callback<ServiceResponse<SharedNoteFull>>() {
        @Override
        public void onResponse(Call<ServiceResponse<SharedNoteFull>> call, Response<ServiceResponse<SharedNoteFull>> response) {
            setWaiting(false);
            if (!response.isSuccessful()) {
                Toast.makeText(
                        NoteActivity.this,
                        response.message()
                                .concat(" (")
                                .concat(String.valueOf(response.code()))
                                .concat(")"),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            ServiceResponse<SharedNoteFull> sharedNoteResponse = response.body();
            if (sharedNoteResponse.isError() && !sharedNoteResponse.containsData()) {
                Toast.makeText(NoteActivity.this, sharedNoteResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            SharedNoteFull sharedNoteFull = sharedNoteResponse.getData();
            setNote(sharedNoteFull);
        }

        @Override
        public void onFailure(Call<ServiceResponse<SharedNoteFull>> call, Throwable t) {
            Toast.makeText(NoteActivity.this, R.string.text_failure, Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
