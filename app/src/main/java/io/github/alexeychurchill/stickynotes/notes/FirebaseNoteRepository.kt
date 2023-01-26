package io.github.alexeychurchill.stickynotes.notes

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.core.NoteEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseNoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : NoteRepository {

    override val allNotes: Flow<List<NoteEntry>>
        get() = flowOf(emptyList())

    override suspend fun getEntry(id: String): NoteEntry? {
        // TODO: Implement getEntry
        return null
    }

    override suspend fun create(entry: NoteEntry): NoteEntry {
        val docRef = firestore.collection(NOTE_ENTRIES_PATH)
            .add(FirestoreNoteEntry(entry))
            .await()
        return entry.copy(id = docRef.id)
    }

    override suspend fun delete(id: String) {
        // TODO: Implement delete
    }

    private companion object {
        const val NOTE_ENTRIES_PATH = "note_entries"
    }
}
