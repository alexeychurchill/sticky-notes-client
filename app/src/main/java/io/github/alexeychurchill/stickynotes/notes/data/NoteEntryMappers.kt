package io.github.alexeychurchill.stickynotes.notes.data

import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

fun RoomNoteEntry.toDomain(): NoteEntry {
    return NoteEntry(
        id = id.toString(),
        ownerId = "",
        title = title,
        subject = subject,
        changedAt = ofTimeMillis(changedAt),
    )
}

fun NoteEntry.toDatabase(): RoomNoteEntry {
    return RoomNoteEntry(
        id = id.toLongOrNull() ?: 0L,
        title = title,
        changedAt = changedAt.timeInMillis,
        subject = subject,
    )
}
