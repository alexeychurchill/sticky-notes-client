package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNote
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNoteText
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_entries ORDER BY changed_at DESC")
    fun allNotesFlow(): Flow<List<RoomNoteEntry>>

    @Query("SELECT * FROM note_entries WHERE id=:id LIMIT 1")
    suspend fun getNoteEntry(id: Long): RoomNoteEntry?

    @Insert(entity = RoomNoteEntry::class, onConflict = REPLACE)
    suspend fun upsertNoteEntry(entry: RoomNoteEntry): Long

    @Query("DELETE FROM note_entries WHERE id=:id")
    suspend fun deleteNoteEntry(id: Long)

    @Query("SELECT * FROM note_texts WHERE id=:id LIMIT 1")
    suspend fun getNoteText(id: Long): RoomNoteText?

    @Insert(entity = RoomNoteText::class, onConflict = REPLACE)
    suspend fun upsertNoteText(entity: RoomNoteText)

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
