package io.github.alexeychurchill.stickynotes.activity.main.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;

/**
 * Base notes fragment
 */

public class BaseNotesFragment extends Fragment implements NotesAdapter.NoteEntryListener, View.OnClickListener {
    private RecyclerView mRVNotes;
    private ProgressBar mPBWait;
    private FloatingActionButton mFab;
    private List<NoteEntry> mNoteEntries = new ArrayList<>();
    private NotesAdapter mNotesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_notes, container, false);
        mRVNotes = ((RecyclerView) view.findViewById(R.id.rvNotes));
        mPBWait = ((ProgressBar) view.findViewById(R.id.pbWait));
        // Adapter
        mNotesAdapter = new NotesAdapter();
        mNotesAdapter.setNoteEntries(mNoteEntries);
        // Layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRVNotes.setLayoutManager(layoutManager);
        EndlessRecyclerViewScrollListener scrollListener =
                new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                onListReachedEnd(page, totalItemsCount, view);
            }
        };
        mRVNotes.addOnScrollListener(scrollListener);
        mRVNotes.setAdapter(mNotesAdapter);
        mFab = ((FloatingActionButton) view.findViewById(R.id.fab));
        if (mFab != null) {
            mFab.setOnClickListener(this);
        }
        onInit();
        return view;
    }

    public List<NoteEntry> getNoteEntries() {
        return mNoteEntries;
    }

    public void notifyDataSetChanged() {
        mNotesAdapter.notifyDataSetChanged();
    }

    public void addNotes(List<NoteEntry> noteEntries) {
        mNoteEntries.addAll(noteEntries);
        notifyDataSetChanged();
    }

    public void clearNotes() {
        mNoteEntries.clear();
        notifyDataSetChanged();
    }

    public void onInit() {
    }

    public void setFabVisible(boolean visible) {
        mFab.setVisibility((visible) ? View.VISIBLE : View.INVISIBLE);
    }

    public void setFabIcon(int resId) {
        mFab.setImageResource(resId);
    }

    public void setWaiting(boolean waiting) {
        mPBWait.setVisibility((waiting) ? View.VISIBLE : View.INVISIBLE);
        mRVNotes.setVisibility((waiting) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            onFabClick();
        }
    }

    public void onFabClick() {
    }

    @Override
    public void onNoteOpen(NoteEntry noteEntry) {
    }

    @Override
    public void onNoteDelete(NoteEntry noteEntry) {
    }

    public void onListReachedEnd(int page, int totalItemsCount, RecyclerView view) {
    }
}
