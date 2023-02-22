package io.github.alexeychurchill.stickynotes.note_editor.domain

data class NoteEdit(
    val id: String,
    val title: String,
    val subject: String,
    val text: String,
)
