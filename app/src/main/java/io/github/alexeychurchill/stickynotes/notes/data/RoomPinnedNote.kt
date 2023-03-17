package io.github.alexeychurchill.stickynotes.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "note_pin",
    primaryKeys = ["note_id", "order_num"],
    foreignKeys = [
        ForeignKey(
            entity = RoomNoteEntry::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = CASCADE,
        ),
    ],
)
class RoomPinnedNote(

    @ColumnInfo(name = "note_id")
    val noteId: Long,

    @ColumnInfo(name = "order_num")
    val order: Int,
)
