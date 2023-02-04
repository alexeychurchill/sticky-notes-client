package io.github.alexeychurchill.stickynotes.note_editor.data

import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.notes.data.toDatabase
import io.github.alexeychurchill.stickynotes.notes.data.toDomain

fun RoomNote.toDomain(): Note {
    return Note(
        entry = entry.toDomain(),
        text = text,
    )
}

fun Note.toDatabase(): RoomNote {
    return RoomNote(
        entry = entry.toDatabase(),
        text = text,
    )
}
