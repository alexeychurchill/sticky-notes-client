package io.github.alexeychurchill.stickynotes.core.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource

data class OverflowMenuItem<T>(
    @StringRes
    val titleResId: Int,
    val item: T,
)

infix fun <T> T.withTitle(@StringRes titleResId: Int): OverflowMenuItem<T> {
    return OverflowMenuItem(
        titleResId = titleResId,
        item = this,
    )
}

@Composable
fun <T> OverflowMenu(
    onPick: (T) -> Unit = { },
    items: List<OverflowMenuItem<T>>
) {
    var showMenu by remember { mutableStateOf(false) }
    IconButton(onClick = { showMenu = true }) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
    ) {
        for (item in items) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(item.titleResId),
                    )
                },
                onClick = {
                    onPick(item.item)
                    showMenu = false
                },
            )
        }
    }
}
