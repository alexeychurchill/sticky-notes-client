package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.presentation.NoteEntryAction

@Composable
fun NoteEntryListItemMenu(
    onDismiss: () -> Unit = { },
    onAction: (NoteEntryAction) -> Unit = { },
    isVisible: Boolean = false,
    isPinned: Boolean = false,
    noteEntry: NoteEntry,
) {
    DropdownMenu(
        expanded = isVisible,
        onDismissRequest = onDismiss,
    ) {

        if (!isPinned) {
            Item(titleResId = R.string.screen_note_list_pin, icon = Icons.Rounded.PushPin) {
                onAction(NoteEntryAction.Pin(id = noteEntry.id.toLong()))
            }
        }

        Item(titleResId = R.string.generic_delete, icon = Icons.Rounded.Delete) {
            onAction(NoteEntryAction.Delete(noteEntry))
        }
    }
}

@Composable
private fun Item(
    @StringRes titleResId: Int,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        onClick = onClick,
        text = {
            Text(text = stringResource(titleResId))
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
    )
}
