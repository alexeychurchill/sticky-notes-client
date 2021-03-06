package io.github.alexeychurchill.stickynotes.fragment.notes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.activity.NoteActivity;
import io.github.alexeychurchill.stickynotes.api.AppConfig;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import io.github.alexeychurchill.stickynotes.model.deserializer.NoteEntryListDeserializer;
import io.github.alexeychurchill.stickynotes.model.deserializer.NoteEntryListResponseDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Shared notes fragment
 */

public class SharedNotesFragment extends BaseNotesFragment {
    private int mPage = 0;
    private StickyNotesApi mApi;
    private String mAccessToken;

    @Override
    public void onInit() {
        mAccessToken = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_ACCESS_TOKEN, null);
        // Gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NoteEntryListResponseDeserializer.TYPE, new NoteEntryListResponseDeserializer())
                .registerTypeAdapter(NoteEntryListDeserializer.TYPE, new NoteEntryListDeserializer())
                .create();
        String baseUrl = getActivity()
                .getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(AppConfig.SHARED_BASE_URL, null);
        if (baseUrl == null) {
            Toast.makeText(getActivity(), R.string.text_no_base_url, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(StickyNotesApi.class);
        // Delete button on items
        setShowItemDeleteButton(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void refresh() {
        clearNotes();
        mPage = 0;
        loadDataPage();
    }

    @Override
    public void onListReachedEnd(int page, int totalItemsCount, RecyclerView view) {
        loadDataPage();
    }

    @Override
    public void onNoteOpen(NoteEntry noteEntry) {
        Intent openNoteIntent = new Intent(getContext(), NoteActivity.class);
        openNoteIntent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteEntry.getId());
        openNoteIntent.putExtra(NoteActivity.EXTRA_NOTE_SHARED, true);
        startActivity(openNoteIntent);
    }

    private void loadDataPage() {
        if (mApi == null) {
            return;
        }
        if (mAccessToken == null) {
            Toast.makeText(getActivity(), R.string.text_access_token_is_null, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Call<ServiceResponse<List<NoteEntry>>> call = mApi.sharedList(mAccessToken, mPage);
        call.enqueue(mNoteEntryListCallback);
    }

    private Callback<ServiceResponse<List<NoteEntry>>> mNoteEntryListCallback = new Callback<ServiceResponse<List<NoteEntry>>>() {
        @Override
        public void onResponse(Call<ServiceResponse<List<NoteEntry>>> call, Response<ServiceResponse<List<NoteEntry>>> response) {
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
            ServiceResponse<List<NoteEntry>> noteEntryListResponse = response.body();
            if (!noteEntryListResponse.containsData() && noteEntryListResponse.isError()) {
                Toast.makeText(getActivity(), noteEntryListResponse.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            List<NoteEntry> noteEntries = noteEntryListResponse.getData();
            if (noteEntries != null) {
                if (!noteEntries.isEmpty()) {
                    mPage++;
                    addNotes(noteEntries);
                }
            }
        }

        @Override
        public void onFailure(Call<ServiceResponse<List<NoteEntry>>> call, Throwable t) {
            setWaiting(false);
            Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
