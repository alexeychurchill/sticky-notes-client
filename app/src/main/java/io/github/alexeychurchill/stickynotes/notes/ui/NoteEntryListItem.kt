@file:OptIn(ExperimentalMaterialApi::class)

package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular

@Composable
fun NoteEntryListItem(
    noteEntry: NoteEntry,
    onEntryClick: (NoteEntry) -> Unit,
    onEntryDelete: (NoteEntry) -> Unit,
) {
    val dismissState = rememberDismissState {
        onEntryDelete(noteEntry)
        true
    }
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(EndToStart),
        dismissThresholds = { FractionalThreshold(0.4f) },
        dismissContent = {
            NoteEntryWidget(
                modifier = Modifier
                    .padding(horizontal = Regular)
                    .padding(top = Regular),
                entry = noteEntry,
                onClick = { onEntryClick(noteEntry) },
            )
        },
        background = {
            DismissBackground(dismissState)
        },
    )
}

@Composable
private fun DismissBackground(
    dismissState: DismissState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Regular)
            .padding(top = Regular),
    ) {
        val color = if (dismissState.dismissDirection == EndToStart) {
            Color.Red
        } else {
            Color.Transparent
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(color = color),
        ) {
            Icon(
                modifier = Modifier
                    .align(CenterEnd)
                    .padding(end = Big)
                    .fillMaxHeight(fraction = 0.33f)
                    .aspectRatio(ratio = 1.0f),
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                tint = MaterialTheme.colors.onError,
            )
        }
    }
}
