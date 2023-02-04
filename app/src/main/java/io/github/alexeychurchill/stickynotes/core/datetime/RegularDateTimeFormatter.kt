package io.github.alexeychurchill.stickynotes.core.datetime

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegularDateTimeFormatter @Inject constructor(): DateTimeFormatter {

    private val formatter by lazy {
        SimpleDateFormat("HH:mm dd/MMM/yyyy", Locale.getDefault())
    }

    override fun format(timeMs: Long): String = formatter.format(timeMs)
}
