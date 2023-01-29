package io.github.alexeychurchill.stickynotes.notes.presentation

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

sealed interface ModalState {
    object None : ModalState

    object InProgress : ModalState

    object CreateNote : ModalState

    data class DeleteNote(val entry: NoteEntry) : ModalState
}
