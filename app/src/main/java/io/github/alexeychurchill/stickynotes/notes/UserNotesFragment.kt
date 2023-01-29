package io.github.alexeychurchill.stickynotes.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import io.github.alexeychurchill.stickynotes.note_editor.NoteActivity
import io.github.alexeychurchill.stickynotes.notes.presentation.UserNotesViewModel
import io.github.alexeychurchill.stickynotes.notes.ui.UserNotesList

/**
 * User notes fragment
 */
@AndroidEntryPoint
class UserNotesFragment : Fragment() {

    private val viewModel by viewModels<UserNotesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.openNoteEvent.collect(::openNote)
        }
    }

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

    private fun openNote(noteId: String) {
        NoteActivity.start(requireContext(), noteId)
    }
}
