package io.github.alexeychurchill.stickynotes.notes.firebase

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class MockNoteEntryRepository @Inject constructor() : NoteEntryRepository {
    override val allNotes: Flow<List<NoteEntry>> = emptyFlow()

    override suspend fun getEntry(id: String): NoteEntry? = null

    override suspend fun create(entry: NoteEntry): NoteEntry? = null

    override suspend fun delete(id: String) = Unit
}
