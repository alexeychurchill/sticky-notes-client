package io.github.alexeychurchill.stickynotes.core.datetime

interface DateTimeFormatter {
    fun format(timeMs: Long): String
}
