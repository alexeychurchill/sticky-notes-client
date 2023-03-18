
package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.presentation.NoteEntryAction

@Composable
fun NoteEntryListItem(
    modifier: Modifier = Modifier,
    onEntryAction: (NoteEntryAction) -> Unit = { },
    isPinned: Boolean = false,
    noteEntry: NoteEntry,
    onEntryClick: (NoteEntry) -> Unit,
) {
    NoteEntryWidget(
        modifier = modifier,
        entry = noteEntry,
        isPinned = isPinned,
        onClick = { onEntryClick(noteEntry) },
        endItem = {
            var isItemMenuExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = { isItemMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null,
                )
            }

            NoteEntryListItemMenu(
                noteEntry = noteEntry,
                isPinned = isPinned,
                isVisible = isItemMenuExpanded,
                onAction = { action ->
                    onEntryAction(action)
                    isItemMenuExpanded = false
                },
                onDismiss = { isItemMenuExpanded = false },
            )
        },
    )
}
