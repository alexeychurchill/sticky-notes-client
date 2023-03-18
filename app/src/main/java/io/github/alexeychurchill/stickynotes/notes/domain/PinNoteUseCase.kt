package io.github.alexeychurchill.stickynotes.notes.domain

import io.github.alexeychurchill.stickynotes.notes.domain.PinNoteUseCase.Result.LimitReached
import io.github.alexeychurchill.stickynotes.notes.domain.PinNoteUseCase.Result.Ok
import javax.inject.Inject

class PinNoteUseCase @Inject constructor(
    private val noteEntryRepository: NoteEntryRepository,
) {

    suspend operator fun invoke(noteId: Long, force: Boolean): Result {
        // Just check
        noteEntryRepository.getEntry(noteId.toString())
            ?: throw IllegalArgumentException("$noteId doesn't exist!")

        val pinned = noteEntryRepository.getPinned()
        if (!force && pinned.size >= MapPinnedNotesCount) {
            return LimitReached
        }

        val alreadyPinned = pinned
            .take(MapPinnedNotesCount.dec())
            .map { (order, noteId) -> order.inc() to noteId }

        noteEntryRepository.savePinned(listOf(0 to noteId) + alreadyPinned)
        return Ok
    }

    enum class Result {
        Ok,
        LimitReached,
    }
}
