package io.github.alexeychurchill.stickynotes.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.datetime.DateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.*
import io.github.alexeychurchill.stickynotes.notes.presentation.ModalState.*
import io.github.alexeychurchill.stickynotes.notes.presentation.NoteEntryAction.*
import io.github.alexeychurchill.stickynotes.tags.domain.Tag
import io.github.alexeychurchill.stickynotes.tags.domain.TagRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNotesViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val noteEntryRepository: NoteEntryRepository,
    private val noteEntryFactory: NoteEntryFactory,
    private val _dateTimeFormatter: DateTimeFormatter,
    private val tagRepository: TagRepository,
    private val pinNoteUseCase: PinNoteUseCase,
    private val unpinNoteUseCase: UnpinNoteUseCase,
) : ViewModel() {

    private val _openNoteEvent = MutableSharedFlow<String>()

    private val _isInProgress = MutableStateFlow(false)

    private val _isCreateNoteMode = MutableStateFlow(false)

    private val _noteToDelete = MutableStateFlow<NoteEntry?>(null)

    private val _cannotPinDueToLimitId = MutableStateFlow<Long?>(null)

    private val selectedTags = MutableStateFlow(emptySet<String>())

    val dateTimeFormatter: StateFlow<DateTimeFormatter>
        get() = MutableStateFlow(_dateTimeFormatter)

    val tags: StateFlow<List<TagState>> by lazy {
        createTagsState().flowOn(dispatchers.default).stateIn(
            scope = viewModelScope,
            started = Eagerly,
            initialValue = emptyList(),
        )
    }

    val noteEntries: StateFlow<List<NoteEntryUiModel>> by lazy {
        combine(
            createNotesState(),
            noteEntryRepository.pinned,
        ) { notes, pinned ->
            val notesMap = notes.associateBy(keySelector = { it.id.toLong() })
            val pinnedSet = pinned.map(NotePin::second).toSet()

            val pinnedNotes = pinned
                .sortedBy(NotePin::first)
                .map(NotePin::second)
                .mapNotNull { pinnedNoteId -> notesMap[pinnedNoteId] }
                .map { pinnedNote ->
                    NoteEntryUiModel(
                        entry = pinnedNote,
                        isPinned = true,
                    )
                }

            val notPinnedNotes = notes
                .filterNot { entry -> pinnedSet.contains(entry.id.toLong()) }
                .map { entry ->
                    NoteEntryUiModel(
                        entry = entry,
                        isPinned = false,
                    )
                }

            pinnedNotes + notPinnedNotes
        }
            .flowOn(dispatchers.default)
            .stateIn(
                scope = viewModelScope,
                started = Eagerly,
                initialValue = emptyList(),
            )
    }

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

    val cannotPinDueToLimitId: StateFlow<Long?>
        get() = _cannotPinDueToLimitId

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

    fun handleNoteAction(action: NoteEntryAction) {
        viewModelScope.launch {
            when (action) {
                is Delete -> _noteToDelete.emit(action.entry)

                is Pin -> pinNote(action.id)

                is Unpin -> unpinNoteUseCase(action.id)
            }
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

    fun toggleTag(name: String) {
        viewModelScope.launch {
            selectedTags.update { selected ->
                if (selected.contains(name)) selected - name else selected + name
            }
        }
    }

    fun proceedPinOverLimit(cannotPinDueToLimitId: Long) {
        viewModelScope.launch {
            pinNoteUseCase(cannotPinDueToLimitId, true)
            _cannotPinDueToLimitId.emit(null)
        }
    }

    fun cancelPinOverLimit() {
        viewModelScope.launch {
            _cannotPinDueToLimitId.emit(null)
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

    private fun createTagsState(): Flow<List<TagState>> = combine(
        tagRepository.allTags.map { tags -> tags.map(Tag::name) },
        selectedTags,
    ) { allTags, selectedTags ->
        val selected = allTags.filter(selectedTags::contains).map { tagName ->
            TagState(name = tagName, selected = true)
        }

        val nonSelected = allTags.filterNot(selectedTags::contains).map { tagName ->
            TagState(name = tagName, selected = false)
        }

        selected + nonSelected
    }

    private fun createNotesState(): Flow<List<NoteEntry>> = combine(
        createTagFilteredNotesState(),
        noteEntryRepository.allNotes,
    ) { visibleNoteIds, allNotes ->

        if (visibleNoteIds.isEmpty()) {
            return@combine allNotes
        }

        allNotes.filter { noteEntry -> visibleNoteIds.contains(noteEntry.id.toLong()) }
    }

    private fun createTagFilteredNotesState(): Flow<Set<Long>> = combine(
        tagRepository.allTagsNotes,
        selectedTags,
    ) { tagsNotes, selectedTags ->
        tagsNotes
            .filter { tagNote -> selectedTags.contains(tagNote.first.name) }
            .map { tagNote -> tagNote.second }
            .toSet()
    }

    private suspend fun pinNote(noteId: Long) {
        val pinResult = pinNoteUseCase(noteId, false)
        if (pinResult == PinNoteUseCase.Result.LimitReached) {
            _cannotPinDueToLimitId.emit(noteId)
            return
        }
    }

    private suspend fun handleError(e: Throwable) {
        // TODO: Implement error handling
    }
}
