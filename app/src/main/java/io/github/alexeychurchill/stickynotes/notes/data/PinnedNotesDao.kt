package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PinnedNotesDao {
    @Query("SELECT * FROM note_pin ORDER BY order_num ASC")
    fun flowAll(): Flow<List<RoomPinnedNote>>

    @Query("SELECT * FROM note_pin ORDER BY order_num ASC")
    fun getAll(): List<RoomPinnedNote>

    @Insert(onConflict = REPLACE)
    fun insert(pinned: List<RoomPinnedNote>)

    @Query("DELETE FROM note_pin")
    fun deleteAll()
}
