package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import io.github.alexeychurchill.stickynotes.R

@Composable
fun PinLimitStateDialog(
    onProceed: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.dialog_proceed_pin_title))
        },
        text = {
            Text(text = stringResource(R.string.dialog_proceed_pin_text))
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        confirmButton = {
            TextButton(
                onClick = onProceed,
            ) {
                Text(
                    text = stringResource(R.string.generic_proceed).uppercase(),
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
