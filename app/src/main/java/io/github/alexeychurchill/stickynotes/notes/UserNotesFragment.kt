package io.github.alexeychurchill.stickynotes.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.activity.NoteActivity
import io.github.alexeychurchill.stickynotes.dialog.ConfirmDeleteNoteDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.CreateNoteDialogFragment
import io.github.alexeychurchill.stickynotes.model.OldNoteEntry
import io.github.alexeychurchill.stickynotes.notes.NotesState.Loaded
import io.github.alexeychurchill.stickynotes.notes.NotesState.Loading
import kotlinx.coroutines.flow.map

/**
 * User notes fragment
 */
class UserNotesFragment : BaseNotesFragment() {

    private val viewModel by viewModels<NotesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabIcon(R.drawable.ic_add_white_36dp)
        setFabVisible(true)

        // Progress bar
        lifecycleScope.launchWhenStarted {
            viewModel.notesState
                .map { it is Loading }
                .collect(::setWaiting)
        }

        // Notes items
        lifecycleScope.launchWhenStarted {
            viewModel.notesState.collect { state ->
                if (state is Loaded) {
                    clearNotes()
                    TODO("Get rid of BaseNotesFragment dependency")
                    /*addNotes(state.items)*/
                }
            }
        }
    }

    override fun onNoteOpen(noteEntry: OldNoteEntry) {
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteEntry.id)
        intent.putExtra(NoteActivity.EXTRA_NOTE_SHARED, false)
        startActivity(intent)
    }

    override fun onNoteDelete(noteEntry: OldNoteEntry) {
        val confirmDeleteNoteDialogFragment = ConfirmDeleteNoteDialogFragment()
        confirmDeleteNoteDialogFragment.setNote(noteEntry)
        confirmDeleteNoteDialogFragment.setListener {
            TODO("Refactor ConfirmDeleteNoteDialog")
            /*viewModel.deleteNote(it.id)*/
        }
        confirmDeleteNoteDialogFragment
            .show(childFragmentManager, "ConfirmDeleteNoteDialogFragment")
    }

    override fun onFabClick() {
        val dialog = CreateNoteDialogFragment()
        dialog.setListener {
            viewModel.createNote(it)
        }
        dialog.show(requireActivity().supportFragmentManager, "CreateNoteDialogFragment")
    }
}
