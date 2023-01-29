package io.github.alexeychurchill.stickynotes.note_editor.domain

import io.github.alexeychurchill.stickynotes.core.model.NoteContent

interface NoteContentRepository {

    suspend fun getNoteContent(id: String): NoteContent?
}
