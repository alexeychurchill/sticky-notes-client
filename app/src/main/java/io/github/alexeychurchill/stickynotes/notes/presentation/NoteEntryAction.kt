package io.github.alexeychurchill.stickynotes.notes.presentation

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

sealed class NoteEntryAction(
    open val id: Long,
) {

    data class Delete(val entry: NoteEntry) : NoteEntryAction(entry.id.toLong())

    data class Pin(override val id: Long) : NoteEntryAction(id)
}
