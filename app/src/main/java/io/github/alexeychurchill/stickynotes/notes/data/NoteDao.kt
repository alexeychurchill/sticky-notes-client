package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNote
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNoteText
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_entries ORDER BY changed_at DESC")
    fun allNotesFlow(): Flow<List<RoomNoteEntry>>

    @Query("SELECT * FROM note_entries WHERE id=:id LIMIT 1")
    suspend fun getNoteEntry(id: Long): RoomNoteEntry?

    @Upsert(entity = RoomNoteEntry::class)
    suspend fun upsertNoteEntry(entry: RoomNoteEntry): Long

    @Query("DELETE FROM note_entries WHERE id=:id")
    suspend fun deleteNoteEntry(id: Long)

    @Query("SELECT * FROM note_texts WHERE id=:id LIMIT 1")
    suspend fun getNoteText(id: Long): RoomNoteText?

    @Upsert
    suspend fun upsertNoteText(entity: RoomNoteText): Long

    @Transaction
    suspend fun getNote(id: Long): RoomNote? {
        val entryEntity = getNoteEntry(id) ?: return null
        val text = getNoteText(id)?.text ?: ""
        return RoomNote(entryEntity, text)
    }

    @Transaction
    suspend fun upsertNote(note: RoomNote): Long {
        val noteEntryId = upsertNoteEntry(note.entry)
        val noteTextEntity = RoomNoteText(
            id = noteEntryId,
            text = note.text,
        )
        upsertNoteText(noteTextEntity)
        return noteEntryId
    }
}
