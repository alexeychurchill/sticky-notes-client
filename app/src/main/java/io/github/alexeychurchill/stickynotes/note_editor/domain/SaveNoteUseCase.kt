package io.github.alexeychurchill.stickynotes.note_editor.domain

import io.github.alexeychurchill.stickynotes.core.datetime.Now
import io.github.alexeychurchill.stickynotes.core.extension.ofTimeMillis
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.tags.domain.ExtractTagsUseCase
import io.github.alexeychurchill.stickynotes.tags.domain.TagRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val now: Now,
    private val noteRepository: NoteRepository,
    private val tagRepository: TagRepository,
    private val extractTagsUseCase: ExtractTagsUseCase,
) {
    suspend operator fun invoke(edit: NoteEdit) {
        val noteId = edit.id.toLongOrNull() ?: return
        val noteEntry = NoteEntry(
            id = edit.id,
            title = edit.title,
            subject = edit.subject.takeIf(String::isNotBlank),
            changedAt = ofTimeMillis(now()),
        )
        val note = Note(
            entry = noteEntry,
            text = edit.text,
        )
        val tags = extractTagsUseCase(edit.text)
        noteRepository.saveNote(note)
        tagRepository.saveTags(noteId, tags)
    }
}
