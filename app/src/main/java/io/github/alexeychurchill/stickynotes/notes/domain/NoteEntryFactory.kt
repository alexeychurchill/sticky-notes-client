package io.github.alexeychurchill.stickynotes.notes.domain

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry.Companion.NO_ID
import java.util.*
import javax.inject.Inject

class NoteEntryFactory @Inject constructor() {
    suspend fun create(title: String): NoteEntry {
        val now = Calendar.getInstance()
        return NoteEntry(
            id = NO_ID,
            ownerId = NO_ID,
            title = title,
            subject = null,
            changedAt = now,
        )
    }
}
