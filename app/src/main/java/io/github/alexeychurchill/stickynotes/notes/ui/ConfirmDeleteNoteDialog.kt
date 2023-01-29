package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.alexeychurchill.stickynotes.R

@Composable
fun ConfirmDeleteNoteDialog(
    noteTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(
                    R.string.dialog_delete_note_text,
                    noteTitle,
                ),
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.dialog_delete_note_text,
                    noteTitle,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.generic_delete).uppercase(),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.generic_cancel).uppercase(),
                )
            }
        },
        onDismissRequest = onDismiss,
    )
}
