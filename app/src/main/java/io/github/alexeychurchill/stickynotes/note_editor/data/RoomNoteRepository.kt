package io.github.alexeychurchill.stickynotes.note_editor.data

import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import io.github.alexeychurchill.stickynotes.notes.data.toDatabase
import io.github.alexeychurchill.stickynotes.notes.data.toDomain
import kotlinx.coroutines.withContext
import java.util.concurrent.Callable
import javax.inject.Inject

class RoomNoteRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val db: Database,
) : NoteRepository {

    override suspend fun getNote(id: String): Note? {
        val noteId = id.toLongOrNull() ?: return null
        return withContext(dispatchers.io) {
            db.runInTransaction(getNoteTransaction(noteId))
        }
    }

    override suspend fun saveNote(note: Note) = withContext(dispatchers.io) {
        val entryEntity = note.entry.toDatabase()
        val textEntity = RoomNoteText(
            id = entryEntity.id,
            text = note.text,
        )
        db.runInTransaction {
            db.noteEntryDao().insertNoteEntry(entryEntity)
            db.noteTextDao().insertNoteText(textEntity)
        }
    }

    private fun getNoteTransaction(noteId: Long): Callable<Note?> = Callable<Note?> {
        val noteEntry = db.noteEntryDao().getNoteEntry(noteId)
            ?: return@Callable null

        val text = db.noteTextDao().getNoteText(noteEntry.id)
            ?.text ?: ""

        return@Callable Note(
            entry = noteEntry.toDomain(),
            text = text,
        )
    }
}
