package io.github.alexeychurchill.stickynotes.notes.presentation

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

data class NoteEntryUiModel(
    val entry: NoteEntry,
    val isPinned: Boolean,
)
