package io.github.alexeychurchill.stickynotes.tags.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import io.github.alexeychurchill.stickynotes.notes.data.RoomNoteEntry

@Entity(
    tableName = "tags",
    primaryKeys = ["note_id", "name"],
    indices = [
        Index(
            value = ["name"],
        ),
    ],
    foreignKeys = [
        ForeignKey(
            entity = RoomNoteEntry::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE,
        ),
    ],
)
data class RoomTag(
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "note_id")
    val noteId: Long,

    @ColumnInfo(name = "name")
    val name: String,
)
