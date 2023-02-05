package io.github.alexeychurchill.stickynotes.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import io.github.alexeychurchill.stickynotes.note_editor.NoteActivity
import io.github.alexeychurchill.stickynotes.notes.presentation.UserNotesViewModel
import io.github.alexeychurchill.stickynotes.notes.ui.UserNotesList

/**
 * Main application activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<UserNotesViewModel>()

            LaunchedEffect(key1 = null) {
                viewModel.openNoteEvent.collect { noteId ->
                    NoteActivity.start(this@MainActivity, noteId)
                }
            }

            StickyNotesTheme {
                UserNotesList(viewModel)
            }
        }
    }
}
