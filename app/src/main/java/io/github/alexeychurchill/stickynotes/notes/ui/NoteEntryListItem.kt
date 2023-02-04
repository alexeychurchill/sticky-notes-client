@file:OptIn(ExperimentalMaterialApi::class)

package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.ShiftToReveal
import io.github.alexeychurchill.stickynotes.core.ui.ShiftValue
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Large
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.specialColors

@Composable
fun NoteEntryListItem(
    noteEntry: NoteEntry,
    onEntryClick: (NoteEntry) -> Unit,
    onEntryDelete: (NoteEntry) -> Unit,
) {
    ShiftToReveal(
        modifier = Modifier.fillMaxWidth(),
        shifts = mapOf(
            96.dp to ShiftValue.EndToStart,
        ),
        content = {
            NoteEntryWidget(
                modifier = Modifier
                    .padding(horizontal = Regular)
                    .padding(top = Regular),
                entry = noteEntry,
                onClick = { onEntryClick(noteEntry) },
            )
        },
        background = {
            ShiftBackground(
                modifier = Modifier.fillMaxWidth(),
                onDeleteClick = { onEntryDelete(noteEntry) },
            )
        },
    )
}

@Composable
private fun ShiftBackground(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = { },
) {
    Box(modifier = modifier) {
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Regular)
                .padding(horizontal = Regular)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = MaterialTheme.specialColors.delete,
                    ),
                    onClick = onDeleteClick,
                )
                .border(
                    width = 3.dp,
                    color = MaterialTheme.specialColors.delete,
                    shape = NoteEntryShape,
                ),
        ) {
            Icon(
                modifier = Modifier
                    .align(CenterEnd)
                    .padding(end = Large)
                    .fillMaxHeight(fraction = 0.33f)
                    .aspectRatio(ratio = 1.0f),
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                tint = MaterialTheme.specialColors.delete,
            )
        }
    }
}
