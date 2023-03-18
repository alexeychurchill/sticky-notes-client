package io.github.alexeychurchill.stickynotes.notes.domain

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import kotlinx.coroutines.flow.Flow

interface NoteEntryRepository {

    val allNotes: Flow<List<NoteEntry>>

    val pinned: Flow<List<NotePin>>

    suspend fun getEntry(id: String): NoteEntry?

    suspend fun create(entry: NoteEntry): NoteEntry?

    suspend fun delete(id: String)

    suspend fun getPinned(): List<NotePin>

    suspend fun savePinned(pinned: List<NotePin>)
}
