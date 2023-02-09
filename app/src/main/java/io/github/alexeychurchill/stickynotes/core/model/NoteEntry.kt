package io.github.alexeychurchill.stickynotes.core.model

import java.util.*

data class NoteEntry(
    val id: String,
    val title: String,
    val subject: String? = null,
    val changedAt: Calendar,
) {
    companion object {
        const val NO_ID = ""
    }
}
