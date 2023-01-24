package io.github.alexeychurchill.stickynotes.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _isInProgress = MutableStateFlow(false)

    val notesState: Flow<NotesState>
        get() = noteRepository.allNotes
            .map { NotesState.items(it) }
            .onStart { NotesState.loading() }
            .catch { emit(NotesState.error(it)) }

    val isInProgress: Flow<Boolean>
        get() = _isInProgress

    /** TODO: Move note creation to upper level **/
    fun createNote(title: String) {
        viewModelScope.launch {
            _isInProgress.emit(true)
            noteRepository.create(title)
            _isInProgress.emit(false)
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
}
