package io.github.alexeychurchill.stickynotes.core.extensions

import java.util.*

fun ofTimeMillis(timeMs: Long): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = timeMs
    }
}
