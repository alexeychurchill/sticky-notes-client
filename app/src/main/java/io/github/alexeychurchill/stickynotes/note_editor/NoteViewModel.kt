package io.github.alexeychurchill.stickynotes.note_editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteContentRepository
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption.COMMENTS
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteEntryRepository: NoteEntryRepository,
    private val noteContentRepository: NoteContentRepository,
) : ViewModel() {

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
        get() = MutableStateFlow(NoteOption.values().toSet())

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

    fun initialise(id: String) {
        // TODO: Start view model
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
        // TODO: Save Note
    }

    fun exit() {
        viewModelScope.launch {
            _onExitEvent.emit(Unit)
        }
    }
}
