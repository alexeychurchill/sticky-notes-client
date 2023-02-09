package io.github.alexeychurchill.stickynotes.app

import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys

sealed class Route(
    val routePath: String,
) {
    companion object {
        val Start: Route = NoteList
    }

    object NoteList : Route(
        routePath = "note_list",
    )

    data class NoteEditor(val noteId: String) : Route(
        routePath = "$Path/${noteId}",
    ) {
        companion object {
            const val Path = "note_editor"
            const val PathTemplate = "$Path/{${NoteKeys.NoteId}}"
        }
    }
}
