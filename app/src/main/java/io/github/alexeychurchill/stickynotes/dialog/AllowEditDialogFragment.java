package io.github.alexeychurchill.stickynotes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import io.github.alexeychurchill.stickynotes.R;

/**
 * Allow edit dialog fragment
 */

public class AllowEditDialogFragment extends DialogFragment {
    private OnAllowDecisionListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle(R.string.text_title_note_sharing)
                .setMessage(R.string.text_do_you_want_allow_editing_to_this_user)
                .setNeutralButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onAllowEditDecision(true);
                        }
                    }
                })
                .setNegativeButton(R.string.text_button_forbid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onAllowEditDecision(false);
                        }
                    }
                })
                .create();
    }

    public void setListener(OnAllowDecisionListener listener) {
        this.mListener = listener;
    }

    public interface OnAllowDecisionListener {
        void onAllowEditDecision(boolean allowed);
    }
}
