package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.github.alexeychurchill.stickynotes.core.datetime.DateTimeFormatter

@Composable
fun WithDateTimeFormatter(
    formatter: DateTimeFormatter,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDateTimeFormatter provides formatter) {
        content()
    }
}

object CurrentDateTimeFormatter {
    @Composable
    fun format(timeMs: Long): String {
        return LocalDateTimeFormatter.current.format(timeMs)
    }
}

val LocalDateTimeFormatter = compositionLocalOf<DateTimeFormatter> {
    DefaultDateTimeFormatter
}

private object DefaultDateTimeFormatter : DateTimeFormatter {
    override fun format(timeMs: Long): String = timeMs.toString()
}
