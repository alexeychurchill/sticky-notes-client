package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_entries ORDER BY changed_at DESC")
    fun allNotesFlow(): Flow<List<RoomNoteEntry>>

    @Query("SELECT * FROM note_entries WHERE id=:id LIMIT 1")
    fun getNoteEntry(id: Long): RoomNoteEntry?

    @Insert(entity = RoomNoteEntry::class, onConflict = REPLACE)
    fun insertNoteEntry(entry: RoomNoteEntry): Long

    @Query("DELETE FROM note_entries WHERE id=:id")
    fun deleteNoteEntry(id: Long)
}
