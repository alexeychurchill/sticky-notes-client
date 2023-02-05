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
import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys.NoteId
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteViewModel
import io.github.alexeychurchill.stickynotes.note_editor.ui.NoteScreen

/**
 * Note view and edit activity
 */
@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {

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
            viewModel.onExitEvent.collect {
                finish()
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, noteId: String) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                putExtra(NoteId, noteId)
            }
            context.startActivity(intent)
        }
    }
}
