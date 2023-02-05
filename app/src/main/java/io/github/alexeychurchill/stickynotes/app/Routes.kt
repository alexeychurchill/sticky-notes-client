package io.github.alexeychurchill.stickynotes.app

sealed class Route(val routePath: String) {
    companion object {
        val Start: Route = NoteList
    }

    object NoteList : Route("note_list")

    data class NoteEditor(val noteId: String) : Route("$Path/${noteId}") {
        companion object {
            val Path = "note_editor"
            val ArgNoteId = "note_id"
            val PathTemplate = "$Path/{${ArgNoteId}}"
        }
    }
}
