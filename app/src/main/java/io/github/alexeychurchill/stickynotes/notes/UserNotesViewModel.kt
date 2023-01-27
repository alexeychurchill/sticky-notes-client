package io.github.alexeychurchill.stickynotes.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.ModalState.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteEntryFactory: NoteEntryFactory,
) : ViewModel() {

    private val _openNoteEvent = MutableSharedFlow<String>()

    private val _isInProgress = MutableStateFlow(false)

    private val _isCreateNoteMode = MutableStateFlow(false)

    private val _noteToDelete = MutableStateFlow<NoteEntry?>(null)

    val notesState: Flow<NotesState>
        get() = noteRepository.allNotes
            .map { NotesState.items(it) }
            .onStart { NotesState.loading() }
            .catch { emit(NotesState.error(it)) }

    val openNoteEvent: Flow<String>
        get() = _openNoteEvent

    val modalState: Flow<ModalState>
        get() = combine(
            _isInProgress,
            _isCreateNoteMode,
            _noteToDelete
        ) { inProgress, isInCreateMode, toDelete ->
            when {
                inProgress -> InProgress
                isInCreateMode -> CreateNote
                toDelete != null -> DeleteNote(toDelete)
                else -> None
            }
        }

    fun reload() {
        /** TODO: Implement Reload **/
    }

    fun openNote(id: String) {
        viewModelScope.launch {
            _openNoteEvent.emit(id)
        }
    }

    /** TODO: Move note creation to upper level **/
    fun createNote() {
        viewModelScope.launch {
            _isCreateNoteMode.emit(true)
        }
    }

    fun confirmCreateNote(title: String) {
        viewModelScope.launch {
            _isCreateNoteMode.emit(false)
            _isInProgress.emit(true)
            val entry = noteEntryFactory.create(title)
            noteRepository.create(entry)
            _isInProgress.emit(false)
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
            val note = noteRepository.getEntry(id) ?: return@launch
            _noteToDelete.emit(note)
            _isInProgress.emit(false)
        }
    }

    fun confirmDeleteNote() {
        val noteToDelete = _noteToDelete.value ?: return
        viewModelScope.launch {
            _isInProgress.emit(true)
            noteRepository.delete(noteToDelete.id)
            _isInProgress.emit(false)
        }
    }

    fun rejectDeleteNote() {
        viewModelScope.launch {
            _noteToDelete.emit(null)
        }
    }
}
