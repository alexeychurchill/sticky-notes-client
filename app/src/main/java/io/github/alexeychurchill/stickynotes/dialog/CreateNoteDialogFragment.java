package io.github.alexeychurchill.stickynotes.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.github.alexeychurchill.stickynotes.R;

/**
 * Create note dialog
 */

public class CreateNoteDialogFragment extends DialogFragment {
    private CreateNoteListener mListener;
    private EditText mETTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mETTitle = new EditText(getContext());
        mETTitle.setHint(R.string.text_hint_title);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder
                .setTitle(R.string.text_title_dialog_create)
                .setView(mETTitle)
                .setNeutralButton(R.string.text_dismiss, null)
                .setPositiveButton(R.string.text_create, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button btnCreate = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            if (mETTitle.getText().toString().isEmpty()) {
                                Toast.makeText(
                                        CreateNoteDialogFragment.this.getContext(),
                                        R.string.text_title_empty,
                                        Toast.LENGTH_SHORT
                                ).show();
                            } else {
                                mListener.onCreateNote(mETTitle.getText().toString());
                            }
                        }
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    public void setListener(CreateNoteListener listener) {
        this.mListener = listener;
    }

    public interface CreateNoteListener {
        void onCreateNote(String title);
    }
}
