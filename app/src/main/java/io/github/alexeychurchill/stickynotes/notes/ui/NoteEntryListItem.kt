
package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular

@Composable
fun NoteEntryListItem(
    noteEntry: NoteEntry,
    onEntryClick: (NoteEntry) -> Unit,
    onEntryDelete: (NoteEntry) -> Unit,
) {
    // TODO: Implement Delete handling
    NoteEntryWidget(
        modifier = Modifier
            .padding(horizontal = Regular)
            .padding(top = Regular),
        entry = noteEntry,
        onClick = { onEntryClick(noteEntry) },
    )
}
