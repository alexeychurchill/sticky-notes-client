package io.github.alexeychurchill.stickynotes.note_editor.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys.NoteId
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteEdit
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import io.github.alexeychurchill.stickynotes.note_editor.domain.SaveNoteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val noteId: String = savedStateHandle[NoteId]
        ?: throw IllegalArgumentException("No parameter $NoteId passed!")

    private val _inProgress = MutableStateFlow(false)

    private val _title = MutableStateFlow("")
    private val _subject = MutableStateFlow("")
    private val _text = MutableStateFlow("")

    private val _onExitEvent = MutableSharedFlow<Unit>()

    val inProgress: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val isSaveEnabled: StateFlow<Boolean>
        get() = MutableStateFlow(true)

    val title: StateFlow<String>
        get() = _title

    val subject: StateFlow<String>
        get() = _subject

    val text: StateFlow<String>
        get() = _text

    val onExitEvent: Flow<Unit>
        get() = _onExitEvent

    init {
        viewModelScope.launch {
            _inProgress.emit(true)
            val note = noteRepository.getNote(noteId) ?: run {
                // TODO: Handle Note load error (Dialog)
                _onExitEvent.emit(Unit)
                return@launch
            }
            val entry = note.entry
            _title.emit(entry.title)
            _subject.emit(entry.subject ?: "")
            _text.emit(note.text)
            _inProgress.emit(false)
        }
    }

    fun onTitleChange(title: String) {
        _title.tryEmit(title)
    }

    fun onSubjectChange(subject: String) {
        _subject.tryEmit(subject)
    }

    fun onTextChange(text: String) {
        _text.tryEmit(text)
    }

    fun saveNote() {
        viewModelScope.launch {
            _inProgress.emit(true)
            saveNoteUseCase(getEdit())
            _inProgress.emit(false)
            _onExitEvent.emit(Unit)
        }
    }

    fun exit() {
        viewModelScope.launch {
            _onExitEvent.emit(Unit)
        }
    }

    private fun getEdit(): NoteEdit {
        return NoteEdit(
            id = noteId,
            title = _title.value,
            subject = _subject.value,
            text = _text.value,
        )
    }
}
