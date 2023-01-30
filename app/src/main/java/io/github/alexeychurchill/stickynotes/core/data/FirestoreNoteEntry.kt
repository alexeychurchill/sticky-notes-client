package io.github.alexeychurchill.stickynotes.core.data

import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

data class FirestoreNoteEntry(
    val ownerId: String = "",
    val title: String = "",
    val subject: String? = null,
    val changedAt: Long = 0L,
) {
    constructor(noteEntry: NoteEntry): this(
        ownerId = noteEntry.ownerId,
        title = noteEntry.title,
        subject = noteEntry.subject,
        changedAt = noteEntry.changedAt.timeInMillis,
    )
}

fun FirestoreNoteEntry.toDomain(id: String): NoteEntry {
    return NoteEntry(
        id = id,
        ownerId = ownerId,
        title = title,
        subject = subject,
        changedAt = ofTimeMillis(changedAt),
    )
}
