package io.github.alexeychurchill.stickynotes.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.datetime.DateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryFactory
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import io.github.alexeychurchill.stickynotes.notes.presentation.ModalState.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNotesViewModel @Inject constructor(
    private val noteEntryRepository: NoteEntryRepository,
    private val noteEntryFactory: NoteEntryFactory,
    private val dispatchers: DispatcherProvider,
    val dateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    private val _openNoteEvent = MutableSharedFlow<String>()

    private val _isInProgress = MutableStateFlow(false)

    private val _isCreateNoteMode = MutableStateFlow(false)

    private val _noteToDelete = MutableStateFlow<NoteEntry?>(null)

    val notesState: Flow<NotesState>
        get() = noteEntryRepository.allNotes
            .map { NotesState.items(it) }
            .flowOn(dispatchers.io)
            .onStart { emit(NotesState.loading()) }
            .catch { emit(NotesState.error(it)) }

    val openNoteEvent: Flow<String>
        get() = _openNoteEvent

    val modalState: Flow<ModalState>
        get() = combine(
            _isInProgress,
            _isCreateNoteMode,
            _noteToDelete,
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
        safeOp {
            _isCreateNoteMode.emit(false)
            val entry = noteEntryFactory.create(title)
            noteEntryRepository.create(entry)
        }
    }

    fun cancelCreateNote() {
        viewModelScope.launch {
            _isCreateNoteMode.emit(false)
        }
    }

    /** TODO: Consider moving to upper level **/
    fun deleteNote(entry: NoteEntry) {
        viewModelScope.launch {
            _noteToDelete.emit(entry)
        }
    }

    fun proceedDeleteNote(noteId: String) {
        safeOp {
            _noteToDelete.emit(null)
            noteEntryRepository.delete(noteId)
        }
    }

    fun rejectDeleteNote() {
        viewModelScope.launch {
            _noteToDelete.emit(null)
        }
    }

    private fun safeOp(
        errorHandler: suspend (Throwable) -> Unit = ::handleError,
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            _isInProgress.emit(true)
            try {
                block()
            } catch (e: Throwable) {
                errorHandler(e)
            } finally {
                _isInProgress.emit(false)
            }
        }
    }

    private suspend fun handleError(e: Throwable) {
        // TODO: Implement error handling
    }
}
