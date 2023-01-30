package io.github.alexeychurchill.stickynotes.note_editor.domain

import io.github.alexeychurchill.stickynotes.core.model.Note

interface NoteContentRepository {

    suspend fun getNote(id: String): Note?

    suspend fun saveNote(note: Note)
}
