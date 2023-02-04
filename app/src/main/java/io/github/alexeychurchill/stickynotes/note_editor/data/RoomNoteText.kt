package io.github.alexeychurchill.stickynotes.note_editor.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import io.github.alexeychurchill.stickynotes.notes.data.RoomNoteEntry

@Entity(
    tableName = "note_texts",
    foreignKeys = [
        ForeignKey(
            entity = RoomNoteEntry::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = CASCADE,
        )
    ],
)
data class RoomNoteText(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "text")
    val text: String,
)
