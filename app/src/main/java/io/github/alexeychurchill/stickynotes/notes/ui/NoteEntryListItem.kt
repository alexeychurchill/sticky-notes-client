
package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry

@Composable
fun NoteEntryListItem(
    modifier: Modifier = Modifier,
    noteEntry: NoteEntry,
    onEntryClick: (NoteEntry) -> Unit,
    onEntryDelete: (NoteEntry) -> Unit,
) {
    NoteEntryWidget(
        modifier = modifier,
        entry = noteEntry,
        onClick = { onEntryClick(noteEntry) },
        endItem = {
            var isItemMenuExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = { isItemMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null,
                )
            }

            DropdownMenu(
                expanded = isItemMenuExpanded,
                onDismissRequest = { isItemMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        onEntryDelete(noteEntry)
                        isItemMenuExpanded = false
                    },
                    text = {
                        Text(text = stringResource(R.string.generic_delete))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                        )
                    },
                )
            }
        },
    )
}
