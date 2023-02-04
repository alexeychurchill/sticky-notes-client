package io.github.alexeychurchill.stickynotes.note_editor.data

import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import javax.inject.Inject

class MockNoteRepository @Inject constructor() : NoteRepository {

    override suspend fun getNote(id: String): Note? = null

    override suspend fun saveNote(note: Note) = Unit
}
