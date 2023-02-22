package io.github.alexeychurchill.stickynotes.tags.data

import androidx.room.ColumnInfo

data class RoomTagUsage(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "use_count")
    val useCount: Int,
)
