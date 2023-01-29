package io.github.alexeychurchill.stickynotes.note_editor

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteContentRepository
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteEntryRepository: NoteEntryRepository,
    private val noteContentRepository: NoteContentRepository,
) : ViewModel() {

    val inProgress: Flow<Boolean>
        get() = flowOf(false)

    val text: Flow<String>
        get() = flowOf("")

    val isEditable: Flow<Boolean>
        get() = flowOf(true)

    fun initialise(id: String) {
        // TODO: Start view model
    }

    fun saveNote() {
        // TODO: Save Note
    }
}
