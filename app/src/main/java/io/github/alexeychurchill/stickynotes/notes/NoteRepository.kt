package io.github.alexeychurchill.stickynotes.notes

import io.github.alexeychurchill.stickynotes.core.NoteEntry
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val allNotes: Flow<List<NoteEntry>>

    suspend fun create(title: String): NoteEntry?

    suspend fun delete(id: String)
}
