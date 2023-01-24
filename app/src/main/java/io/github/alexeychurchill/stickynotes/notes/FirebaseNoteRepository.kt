package io.github.alexeychurchill.stickynotes.notes

import io.github.alexeychurchill.stickynotes.core.NoteEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FirebaseNoteRepository @Inject constructor() : NoteRepository {
    override val allNotes: Flow<List<NoteEntry>>
        get() = flowOf(emptyList())

    override suspend fun create(title: String): NoteEntry? {
       return null
    }

    override suspend fun delete(id: String) {
        // ...
    }
}
