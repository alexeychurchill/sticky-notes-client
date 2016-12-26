package io.github.alexeychurchill.stickynotes.activity.main.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;

/**
 * Notes adapter
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<NoteEntry> mNoteEntries;
    private NoteEntryListener mNoteEntryListener;

    public void setNoteEntries(List<NoteEntry> noteEntries) {
        this.mNoteEntries = noteEntries;
    }

    public void setNoteEntryListener(NoteEntryListener noteEntryListener) {
        this.mNoteEntryListener = noteEntryListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_note_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mNoteEntries.get(position), mNoteEntryListener);
    }

    @Override
    public int getItemCount() {
        return (mNoteEntries == null) ? 0 : mNoteEntries.size();
    }

    public interface NoteEntryListener {
        void onNoteOpen(NoteEntry noteEntry);
        void onNoteDelete(NoteEntry noteEntry);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTVTitle;
        private TextView mTVSubject;
        private TextView mTVDate;
        private NoteEntryListener mNoteEntryListener;
        private NoteEntry mNoteEntry;

        public ViewHolder(View itemView) {
            super(itemView);
            // Views
            mTVTitle = ((TextView) itemView.findViewById(R.id.tvTitle));
            mTVSubject = ((TextView) itemView.findViewById(R.id.tvSubject));
            mTVDate = ((TextView) itemView.findViewById(R.id.tvDate));
            // Buttons
            // Open button
            Button btnOpen = ((Button) itemView.findViewById(R.id.btnOpen));
            if (btnOpen != null) {
                btnOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mNoteEntryListener != null) {
                            mNoteEntryListener.onNoteOpen(mNoteEntry);
                        }
                    }
                });
            }
            // Delete button
            Button btnDelete = ((Button) itemView.findViewById(R.id.btnDelete));
            if (btnDelete != null) {
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mNoteEntryListener != null) {
                            mNoteEntryListener.onNoteDelete(mNoteEntry);
                        }
                    }
                });
            }
        }

        public void bind(NoteEntry noteEntry, NoteEntryListener listener) {
            mNoteEntryListener = listener;
            mNoteEntry = noteEntry;
            mTVTitle.setText(noteEntry.getTitle());
            // Subject
            if (noteEntry.getSubject() == null) {
                mTVSubject.setVisibility(View.GONE);
            } else {
                mTVSubject.setVisibility(View.VISIBLE);
                mTVSubject.setText(noteEntry.getSubject());
            }
            // Date
            String dateString =
                    String.format(Locale.getDefault(), "%1$tH:%1$tM %1$td.%1$tm.%1$tY", noteEntry.getChangedDate());
            mTVDate.setText(dateString);
        }
    }
}
