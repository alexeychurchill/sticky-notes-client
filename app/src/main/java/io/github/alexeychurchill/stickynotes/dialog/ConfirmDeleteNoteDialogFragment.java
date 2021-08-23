package io.github.alexeychurchill.stickynotes.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.NoteEntry;

/**
 * Confirm for note deleting
 */

public class ConfirmDeleteNoteDialogFragment extends DialogFragment {
    private NoteEntry mNote;
    private OnDeleteNoteListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String textFormatter = getString(R.string.text_formatter_delete_note);
        return builder
                .setTitle(R.string.text_title_dialog_delete)
                .setMessage(String.format(Locale.getDefault(), textFormatter,
                        (mNote == null) ? "?" : mNote.getTitle()))
                .setPositiveButton(R.string.text_button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onNoteDeleteConfirmed(mNote);
                        }
                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .create();
    }

    public void setNote(NoteEntry note) {
        this.mNote = note;
    }

    public void setListener(OnDeleteNoteListener listener) {
        this.mListener = listener;
    }

    public interface OnDeleteNoteListener {
        void onNoteDeleteConfirmed(NoteEntry note);
    }
}
