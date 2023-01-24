package io.github.alexeychurchill.stickynotes.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.activity.NoteActivity
import io.github.alexeychurchill.stickynotes.core.StickyNotesTheme
import io.github.alexeychurchill.stickynotes.dialog.ConfirmDeleteNoteDialogFragment
import io.github.alexeychurchill.stickynotes.model.JsonNoteEntry
import io.github.alexeychurchill.stickynotes.notes.ui.UserNotesList

/**
 * User notes fragment
 */
@AndroidEntryPoint
class UserNotesFragment : Fragment() {

    private val viewModel by viewModels<UserNotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as? ComposeView)?.setContent {
            StickyNotesTheme {
                UserNotesList(viewModel)
            }
        }
    }

    /*override*/ fun onNoteOpen(noteEntry: JsonNoteEntry) {
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteEntry.id)
        intent.putExtra(NoteActivity.EXTRA_NOTE_SHARED, false)
        startActivity(intent)
    }

    /*override*/ fun onNoteDelete(noteEntry: JsonNoteEntry) {
        val confirmDeleteNoteDialogFragment = ConfirmDeleteNoteDialogFragment()
        confirmDeleteNoteDialogFragment.setNote(noteEntry)
        confirmDeleteNoteDialogFragment.setListener {
            TODO("Refactor ConfirmDeleteNoteDialog")
            /*viewModel.deleteNote(it.id)*/
        }
        confirmDeleteNoteDialogFragment
            .show(childFragmentManager, "ConfirmDeleteNoteDialogFragment")
    }
}
