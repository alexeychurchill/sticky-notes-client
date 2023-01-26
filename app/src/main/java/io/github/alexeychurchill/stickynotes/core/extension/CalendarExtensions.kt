package io.github.alexeychurchill.stickynotes.core.extension

import java.util.*

fun ofTimeMillis(timeMs: Long): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = timeMs
    }
}
