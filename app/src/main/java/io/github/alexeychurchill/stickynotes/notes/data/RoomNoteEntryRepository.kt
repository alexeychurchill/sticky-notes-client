package io.github.alexeychurchill.stickynotes.notes.data

import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomNoteEntryRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val db: Database,
) : NoteEntryRepository {

    override val allNotes: Flow<List<NoteEntry>>
        get() = db.noteEntryDao()
            .allNotesFlow()
            .map { entities -> entities.map(RoomNoteEntry::toDomain) }
            .flowOn(dispatchers.io)

    override suspend fun getEntry(id: String): NoteEntry? {
        val noteId = id.toLongOrNull() ?: return null
        return db.noteEntryDao().getNoteEntry(noteId)?.toDomain()
    }

    override suspend fun create(entry: NoteEntry): NoteEntry {
        val entity = entry.toDatabase()
        val noteId = db.noteEntryDao().upsertNoteEntry(entity)
        return entry.copy(id = noteId.toString())
    }

    override suspend fun delete(id: String) {
        db.noteEntryDao().deleteNoteEntry(id = id.toLongOrNull() ?: return)
    }
}
