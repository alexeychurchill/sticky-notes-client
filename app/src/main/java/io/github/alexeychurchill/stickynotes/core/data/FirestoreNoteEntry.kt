package io.github.alexeychurchill.stickynotes.core.data

import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry.Fields.ChangedAt
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry.Fields.OwnerId
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry.Fields.Subject
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry.Fields.Title
import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

object FirestoreNoteEntry {
    fun toFirestore(domain: NoteEntry): Map<String, Any?> = with(domain) {
        mapOf(
            OwnerId to ownerId,
            Title to title,
            Subject to subject,
            ChangedAt to changedAt,
        )
    }

    fun toDomain(id: String, values: Map<String, Any?>): NoteEntry = NoteEntry(
        id = id,
        ownerId = values[OwnerId] as String,
        title = values[Title] as String,
        subject = values[Subject] as String?,
        changedAt = ofTimeMillis(values[ChangedAt] as Long),
    )
}
