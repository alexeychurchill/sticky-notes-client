package io.github.alexeychurchill.stickynotes.note_editor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteTextDao {
    @Query("SELECT * FROM note_texts WHERE id=:id LIMIT 1")
    fun getNoteText(id: Long): RoomNoteText?

    @Insert(entity = RoomNoteText::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteText(entity: RoomNoteText)
}
