package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entries")
data class RoomNoteEntry(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "changed_at")
    val changedAt: Long,

    @ColumnInfo(name = "subject")
    val subject: String? = null,
)
