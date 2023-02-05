package io.github.alexeychurchill.stickynotes.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNoteText
import io.github.alexeychurchill.stickynotes.notes.data.NoteDao
import io.github.alexeychurchill.stickynotes.notes.data.RoomNoteEntry

@Database(
    version = 1,
    entities = [
        RoomNoteEntry::class,
        RoomNoteText::class,
    ],
)
abstract class Database : RoomDatabase() {
    abstract fun noteEntryDao(): NoteDao
}
