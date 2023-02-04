package io.github.alexeychurchill.stickynotes.note_editor.data

import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomNoteRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val db: Database,
) : NoteRepository {

    override suspend fun getNote(id: String): Note? {
        return withContext(dispatchers.io) {
            db.noteEntryDao()
                .getNote(id = id.toLongOrNull() ?: return@withContext null)
                ?.toDomain()
        }
    }

    override suspend fun saveNote(note: Note) {
        val entity = note.toDatabase()
        db.noteEntryDao().upsertNote(entity)
    }
}
