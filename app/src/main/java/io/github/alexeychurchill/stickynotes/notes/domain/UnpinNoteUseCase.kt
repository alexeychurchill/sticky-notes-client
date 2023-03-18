package io.github.alexeychurchill.stickynotes.notes.domain

import javax.inject.Inject

class UnpinNoteUseCase @Inject constructor(
    private val noteEntryRepository: NoteEntryRepository,
) {
    suspend operator fun invoke(noteId: Long) {
        // Just check
        noteEntryRepository.getEntry(noteId.toString())
            ?: throw IllegalArgumentException("$noteId doesn't exist!")

        val toSave = noteEntryRepository.getPinned()
            .filter { it.second != noteId }
            .sortedBy(NotePin::first)
            .mapIndexed { index, pin -> index to pin.second }
        noteEntryRepository.savePinned(toSave)
    }
}
