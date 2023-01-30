package io.github.alexeychurchill.stickynotes.note_editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.datetime.Now
import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys.NoteId
import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys.OwnerId
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteContentRepository
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption.COMMENTS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val now: Now,
    private val noteContentRepository: NoteContentRepository,
) : ViewModel() {

    private val noteId: String = savedStateHandle[NoteId]
        ?: throw IllegalArgumentException("No parameter $NoteId passed!")

    private var ownerId: String
        get() = savedStateHandle[OwnerId]
            ?: throw IllegalArgumentException("No $OwnerId!")
        set(value) {
            savedStateHandle[OwnerId] = value
        }

    private val _inProgress = MutableStateFlow(false)

    private val _title = MutableStateFlow("")
    private val _subject = MutableStateFlow("")
    private val _text = MutableStateFlow("")

    private val _onOptionEvent = MutableSharedFlow<NoteOption>()

    private val _onExitEvent = MutableSharedFlow<Unit>()

    val inProgress: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val isEditable: Flow<Boolean>
        get() = flowOf(true)

    val isSaveEnabled: StateFlow<Boolean>
        get() = MutableStateFlow(true)

    val enabledOptions: StateFlow<Set<NoteOption>>
        get() = MutableStateFlow(emptySet())

    val onOptionEvent: Flow<NoteOption>
        get() = _onOptionEvent

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
            val note = noteContentRepository.getNote(noteId) ?: run {
                // TODO: Handle Note load error (Dialog)
                _onExitEvent.emit(Unit)
                return@launch
            }
            val entry = note.entry
            ownerId = entry.ownerId
            _title.emit(entry.title)
            _subject.emit(entry.subject ?: "")
            _text.emit(note.text)
            _inProgress.emit(false)
        }
    }

    fun pickOption(option: NoteOption) {
        viewModelScope.launch {
            if (option == COMMENTS) {
                return@launch
            }
            _onOptionEvent.emit(option)
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
            val noteEntry = NoteEntry(
                id = noteId,
                ownerId = ownerId,
                title = _title.value,
                subject = _subject.value.takeIf(String::isNotBlank),
                changedAt = ofTimeMillis(now()),
            )
            val note = Note(
                entry = noteEntry,
                text = _text.value,
            )
            noteContentRepository.saveNote(note)
            _inProgress.emit(false)
            _onExitEvent.emit(Unit)
        }
    }

    fun exit() {
        viewModelScope.launch {
            _onExitEvent.emit(Unit)
        }
    }
}
