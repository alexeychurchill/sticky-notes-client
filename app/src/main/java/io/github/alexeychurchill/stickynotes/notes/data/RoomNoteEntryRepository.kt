package io.github.alexeychurchill.stickynotes.notes.data

import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import io.github.alexeychurchill.stickynotes.notes.domain.NotePin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    override val pinned: Flow<List<NotePin>>
        get() = db.pinnedDao()
            .flowAll()
            .map { entities ->
                entities
                    .map { pinnedEntity -> pinnedEntity.order to pinnedEntity.noteId }
                    .sortedBy(NotePin::first)
            }
            .flowOn(dispatchers.io)

    override suspend fun getEntry(id: String): NoteEntry? {
        val noteId = id.toLongOrNull() ?: return null
        return withContext(dispatchers.io) {
            db.noteEntryDao().getNoteEntry(noteId)?.toDomain()
        }
    }

    override suspend fun create(entry: NoteEntry): NoteEntry {
        val entity = entry.toDatabase()
        val noteId = withContext(dispatchers.io) {
            db.noteEntryDao().insertNoteEntry(entity)
        }
        return entry.copy(id = noteId.toString())
    }

    override suspend fun delete(id: String) {
        val noteId = id.toLongOrNull() ?: return
        withContext(dispatchers.io) {
            db.noteEntryDao().deleteNoteEntry(noteId)
        }
    }

    override suspend fun getPinned(): List<NotePin> = withContext(dispatchers.io) {
        db.pinnedDao().getAll()
            .map { entity -> entity.order to entity.noteId }
            .sortedBy(NotePin::first)
    }

    override suspend fun savePinned(pinned: List<NotePin>) = withContext(dispatchers.io) {
        db.runInTransaction {
            db.pinnedDao().run {
                deleteAll()
                val pinnedEntities = pinned.map { (order, noteId) ->
                    RoomPinnedNote(order = order, noteId = noteId)
                }
                insert(pinnedEntities)
            }
        }
    }
}
