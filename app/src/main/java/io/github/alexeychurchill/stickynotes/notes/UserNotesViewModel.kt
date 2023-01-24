package io.github.alexeychurchill.stickynotes.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _isInProgress = MutableStateFlow(false)

    private val _isCreateNoteMode = MutableStateFlow(false)

    val notesState: Flow<NotesState>
        get() = noteRepository.allNotes
            .map { NotesState.items(it) }
            .onStart { NotesState.loading() }
            .catch { emit(NotesState.error(it)) }

    val isInProgress: Flow<Boolean>
        get() = _isInProgress

    val isCreateNoteMode: Flow<Boolean>
        get() = _isCreateNoteMode

    /** TODO: Move note creation to upper level **/
    fun createNote() {
        viewModelScope.launch {
            _isCreateNoteMode.emit(true)
        }
    }

    fun confirmCreateNote(title: String) {
        viewModelScope.launch {
            _isCreateNoteMode.emit(false)
        }
    }

    fun cancelCreateNote() {
        viewModelScope.launch {
            _isCreateNoteMode.emit(false)
        }
    }

    /** TODO: Consider moving to upper level **/
    fun deleteNote(id: String) {
        viewModelScope.launch {
            _isInProgress.emit(true)
            noteRepository.delete(id)
            _isInProgress.emit(false)
        }
    }

    fun reload() {
        /** TODO: Implement Reload **/
    }
}
