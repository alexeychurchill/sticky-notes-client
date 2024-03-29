package io.github.alexeychurchill.stickynotes.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.alexeychurchill.stickynotes.note_editor.data.NoteTextDao
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNoteText
import io.github.alexeychurchill.stickynotes.notes.data.NoteDao
import io.github.alexeychurchill.stickynotes.notes.data.PinnedNotesDao
import io.github.alexeychurchill.stickynotes.notes.data.RoomNoteEntry
import io.github.alexeychurchill.stickynotes.notes.data.RoomPinnedNote
import io.github.alexeychurchill.stickynotes.tags.data.RoomTag
import io.github.alexeychurchill.stickynotes.tags.data.TagDao

@Database(
    version = 1,
    entities = [
        RoomNoteEntry::class,
        RoomNoteText::class,
        RoomTag::class,
        RoomPinnedNote::class,
    ],
)
abstract class Database : RoomDatabase() {

    abstract fun noteEntryDao(): NoteDao

    abstract fun noteTextDao(): NoteTextDao

    abstract fun tagDao(): TagDao

    abstract fun pinnedDao(): PinnedNotesDao
}
