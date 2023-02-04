package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteEntryDao {

    @Query("SELECT * FROM note_entries ORDER BY changed_at DESC")
    fun allNotesFlow(): Flow<List<RoomNoteEntry>>

    @Query("SELECT * FROM note_entries WHERE id=:id LIMIT 1")
    suspend fun getNoteEntry(id: Long): RoomNoteEntry?

    @Upsert(entity = RoomNoteEntry::class)
    suspend fun upsertNoteEntry(entry: RoomNoteEntry): Long

    @Query("DELETE FROM note_entries WHERE id=:id")
    suspend fun deleteNoteEntry(id: Long)
}
