package io.github.alexeychurchill.stickynotes.notes

import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

interface NotesState {
    object None : NotesState

    object Loading : NotesState

    data class Error(val error: Throwable) : NotesState

    data class Loaded(val items: List<NoteEntry>) : NotesState

    companion object {

        fun items(items: List<NoteEntry>): NotesState = Loaded(items)

        fun error(thr: Throwable): NotesState = Error(thr)

        fun loading(): NotesState = Loading
    }
}
