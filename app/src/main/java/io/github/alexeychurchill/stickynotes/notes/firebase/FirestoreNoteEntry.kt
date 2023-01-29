package io.github.alexeychurchill.stickynotes.notes.firebase

import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

data class FirestoreNoteEntry(
    val ownerId: String = "",
    val title: String = "",
    val subject: String? = null,
    val createdAt: Long = 0L,
    val changedAt: Long = 0L,
) {
    constructor(noteEntry: NoteEntry): this(
        ownerId = noteEntry.ownerId,
        title = noteEntry.title,
        subject = noteEntry.subject,
        createdAt = noteEntry.createdAt.timeInMillis,
        changedAt = noteEntry.changedAt.timeInMillis,
    )
}

fun FirestoreNoteEntry.toDomain(id: String): NoteEntry {
    return NoteEntry(
        id = id,
        ownerId = ownerId,
        title = title,
        subject = subject,
        createdAt = ofTimeMillis(createdAt),
        changedAt = ofTimeMillis(changedAt),
    )
}
