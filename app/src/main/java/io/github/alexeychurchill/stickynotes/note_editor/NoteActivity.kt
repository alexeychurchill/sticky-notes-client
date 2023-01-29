package io.github.alexeychurchill.stickynotes.note_editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import io.github.alexeychurchill.stickynotes.dialog.EditNoteMetadataDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.ShareNoteDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.SharedToDialogFragment
import io.github.alexeychurchill.stickynotes.model.NoteFull
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption
import io.github.alexeychurchill.stickynotes.note_editor.ui.NoteScreen

/**
 * Note view and edit activity
 */
@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {
    private var mNote: NoteFull? = null
    private var mNoteId = -1

    private val noteId by lazy {
        intent.getStringExtra(EXTRA_NOTE_ID) ?: ""
    }

    private val viewModel by viewModels<NoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StickyNotesTheme {
                NoteScreen()
            }
        }

        // Observe ViewModel
        lifecycleScope.launchWhenCreated {
            viewModel.onOptionEvent.collect(::handleNoteOption)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.onExitEvent.collect {
                finish()
            }
        }

        // TODO: Forward ID to viewModel
        /** viewModel.initialise() **/
    }

    private fun handleNoteOption(option: NoteOption) {
        when(option) {
            NoteOption.EDIT_METADATA -> editMetadata()
            NoteOption.SHARE_WITH -> shareThisNote()
            NoteOption.SHARED_TO -> sharedTo()
            else -> { /** TODO: Implement comments **/ }
        }
    }

    private fun editMetadata() {
        val dialog = EditNoteMetadataDialogFragment()
        dialog.setNote(mNote)
        dialog.show(supportFragmentManager, "EditNoteMetadataDialogFragment")
    }

    private fun sharedTo() {
        val dialog = SharedToDialogFragment()
        dialog.setNoteId(mNoteId)
        dialog.show(supportFragmentManager, "SharedToDialogFragment")
    }

    private fun shareThisNote() {
        val dialog = ShareNoteDialogFragment()
        dialog.setNoteId(mNoteId)
        dialog.show(supportFragmentManager, "ShareNoteDialogFragment")
    }

    companion object {
        private const val EXTRA_NOTE_ID = "note_id"

        @JvmStatic
        fun start(context: Context, noteId: String) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE_ID, noteId)
            }
            context.startActivity(intent)
        }
    }
}
