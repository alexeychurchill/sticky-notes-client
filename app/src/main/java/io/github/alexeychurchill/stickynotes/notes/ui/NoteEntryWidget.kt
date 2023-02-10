package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import io.github.alexeychurchill.stickynotes.core.datetime.RegularDateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import java.util.*

val NoteEntryShape: Shape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.large

@Composable
fun NoteEntryWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    entry: NoteEntry,
) {
    Box(modifier = modifier) {
        val shape = NoteEntryShape
        val backgroundColor = MaterialTheme.colorScheme
            .primary
            .copy(alpha = 0.3f)
            .compositeOver(MaterialTheme.colorScheme.surface)

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
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = Ellipsis,
                )

                Text(
                    text = entry.subject ?: "",
                    style = MaterialTheme.typography.bodyLarge,
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
                        style = MaterialTheme.typography.bodyMedium,
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
