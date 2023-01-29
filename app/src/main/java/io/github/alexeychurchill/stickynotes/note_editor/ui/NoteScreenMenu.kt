package io.github.alexeychurchill.stickynotes.note_editor.ui

import androidx.compose.runtime.Composable
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.ui.OverflowMenu
import io.github.alexeychurchill.stickynotes.core.ui.withTitle
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteOption.*

private val NoteScreenMenuList = listOf(
    EDIT_METADATA withTitle R.string.text_menu_edit_metadata,
    SHARE_WITH withTitle R.string.text_menu_share_note_with,
    SHARED_TO withTitle R.string.text_menu_shared_to,
    COMMENTS withTitle R.string.text_menu_comments,
)

@Composable
fun NoteScreenMenu(
    enabledOptions: Set<NoteOption>,
    onPickOption: (NoteOption) -> Unit,
) {
    if (enabledOptions.isEmpty()) {
        return
    }

    val options = NoteScreenMenuList.filter {
        enabledOptions.contains(it.item)
    }

    OverflowMenu(
        onPick = onPickOption,
        items = options,
    )
}
