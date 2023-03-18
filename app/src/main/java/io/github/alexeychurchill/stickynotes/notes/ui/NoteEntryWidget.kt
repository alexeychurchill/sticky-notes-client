package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.core.datetime.RegularDateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import java.util.*

val NoteEntryShape: Shape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraLarge

@Composable
fun NoteEntryWidget(
    modifier: Modifier = Modifier,
    isPinned: Boolean = false,
    onClick: () -> Unit = { },
    endItem: @Composable (() -> Unit)? = null,
    entry: NoteEntry,
) {
    Box(modifier = modifier) {
        val shape = NoteEntryShape
        val backgroundColor = MaterialTheme.colorScheme.secondaryContainer

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .clickable(onClick = onClick),
            color = backgroundColor,
            shape = shape,
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(weight = 1.0f)
                        .padding(
                            vertical = Medium,
                        )
                        .padding(
                            start = Big,
                            end = endItem?.let { Regular } ?: Big,
                        ),
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (isPinned) {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    .aspectRatio(ratio = 1.0f)
                                    .align(CenterVertically),
                                imageVector = Icons.Rounded.PushPin,
                                contentDescription = null,
                            )

                            Spacer(
                                modifier = Modifier.size(
                                    width = Regular, height = 0.dp,
                                )
                            )
                        }

                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = Ellipsis,
                        )
                    }

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
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                endItem?.let { itemBlock ->
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(CenterVertically)
                            .padding(end = Regular),
                    ) {
                        itemBlock()
                    }
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
                    ),
                     isPinned = true,
                )
            }
        }
    }
}
