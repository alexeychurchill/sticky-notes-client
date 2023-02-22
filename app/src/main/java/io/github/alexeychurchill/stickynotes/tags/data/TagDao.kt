package io.github.alexeychurchill.stickynotes.tags.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tags")
    fun flowAll(): Flow<List<RoomTag>>

    @Query("SELECT name, COUNT(*) AS use_count FROM tags GROUP BY name ORDER BY use_count DESC")
    fun flowUniqueSortByUsage(): Flow<List<RoomTagUsage>>

    @Insert(onConflict = REPLACE)
    fun insertAll(entities: List<RoomTag>)

    @Query("DELETE FROM tags WHERE note_id = :noteId")
    fun deleteAllByNoteId(noteId: Long)
}
