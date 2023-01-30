package io.github.alexeychurchill.stickynotes.note_editor.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.core.data.FirestoreNoteEntry
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteContent
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteContentRepository
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * TODO: Rewrite using transactions
 */
class FirebaseNoteContentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val noteEntryRepository: NoteEntryRepository,
) : NoteContentRepository {

    override suspend fun getNote(id: String): Note? {
        val noteEntry = noteEntryRepository.getEntry(id) ?: return null
        val noteDocument = firestore.collection(NoteContent.Path)
            .document(id)
            .get()
            .await()
        return if (noteDocument.exists()) {
            Note(
                entry = noteEntry,
                text = noteDocument.getString(NoteContent.Fields.Text) ?: "",
            )
        } else {
            Note(
                entry = noteEntry,
                text = "",
            )
        }
    }

    override suspend fun saveNote(note: Note) {
        val id = note.entry.id
        val entryDocRef = firestore
            .collection(NoteEntry.Path)
            .document(id)

        entryDocRef.set(FirestoreNoteEntry.toFirestore(note.entry)).await()

        val contentDocRef = firestore
            .collection(NoteContent.Path)
            .document(id)

        val content = mapOf(
            NoteContent.Fields.Text to note.text,
        )
        contentDocRef.set(content).await()
    }
}
