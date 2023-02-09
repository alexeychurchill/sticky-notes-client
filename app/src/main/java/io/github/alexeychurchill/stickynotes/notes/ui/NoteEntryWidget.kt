package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.core.datetime.RegularDateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import java.util.*

val NoteEntryShape = RoundedCornerShape(16.dp)

@Composable
fun NoteEntryWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    entry: NoteEntry,
) {
    Box(modifier = modifier) {
        val shape = NoteEntryShape
        val backgroundColor = MaterialTheme.colors
            .primary
            .copy(alpha = 0.3f)
            .compositeOver(MaterialTheme.colors.surface)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .clickable(onClick = onClick),
            color = backgroundColor,
            shape = shape,
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = Medium,
                        vertical = Regular,
                    ),
            ) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.h5,
                    maxLines = 1,
                    overflow = Ellipsis,
                )

                Text(
                    text = entry.subject ?: "",
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = Ellipsis,
                )

                val date = CurrentDateTimeFormatter
                    .format(entry.changedAt.timeInMillis)

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .padding(top = Medium)
                            .align(CenterEnd),
                        text = date,
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
    }
}

@Preview(name = "Note entry", widthDp = 360)
@Composable
private fun NoteEntry_Preview() {
    val someTime = Calendar.getInstance().apply {
        set(2023, 2, 2, 16, 35, 43)
    }
    WithDateTimeFormatter(RegularDateTimeFormatter()) {
        StickyNotesTheme {
            Surface {
                NoteEntryWidget(
                    modifier = Modifier.padding(Regular),
                    entry = NoteEntry(
                        id = NoteEntry.NO_ID,
                        title = "Test entry",
                        subject = "Test subject",
                        changedAt = someTime,
                    )
                )
            }
        }
    }
}
