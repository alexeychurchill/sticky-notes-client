package io.github.alexeychurchill.stickynotes.notes

import com.google.firebase.firestore.PropertyName
import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

data class FirestoreNoteEntry(
    @PropertyName("owner_id")
    val ownerId: String = "",
    @PropertyName("title")
    val title: String = "",
    @PropertyName("subject")
    val subject: String? = null,
    @PropertyName("created_at")
    val createdAt: Long = 0L,
    @PropertyName("changed_at")
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
