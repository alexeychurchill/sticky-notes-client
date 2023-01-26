package io.github.alexeychurchill.stickynotes.core

import java.util.*

data class NoteEntry(
    val id: String,
    val ownerId: String,
    val title: String,
    val subject: String? = null,
    val createdAt: Calendar,
    val changedAt: Calendar,
) {
    companion object {
        const val NO_ID = ""
    }
}
