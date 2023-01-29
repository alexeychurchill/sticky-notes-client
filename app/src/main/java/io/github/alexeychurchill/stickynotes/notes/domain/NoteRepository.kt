package io.github.alexeychurchill.stickynotes.notes.domain

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val allNotes: Flow<List<NoteEntry>>

    suspend fun getEntry(id: String): NoteEntry?

    suspend fun create(entry: NoteEntry): NoteEntry?

    suspend fun delete(id: String)
}
