package io.github.alexeychurchill.stickynotes.note_editor.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.core.data.FirestoreNoteEntry
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteContent
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.NoteEntry
import io.github.alexeychurchill.stickynotes.core.model.Note
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * TODO: Rewrite using transactions
 */
class FirebaseNoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : NoteRepository {

    override suspend fun getNote(id: String): Note? {
        val transactionTask = firestore.runTransaction<Note?> { transaction ->
            val entryDocRef = firestore.collection(NoteEntry.Path).document(id)

            val entrySnapshot = transaction.get(entryDocRef)
            val noteEntry = entrySnapshot.data
                ?.let { data -> FirestoreNoteEntry.toDomain(entrySnapshot.id, data) }
                ?: return@runTransaction null

            val contentDocRef = entrySnapshot
                .getDocumentReference(NoteEntry.Fields.Content)

            val text = contentDocRef?.let(transaction::get)
                ?.getString(NoteContent.Fields.Text)
                ?: ""

            Note(
                entry = noteEntry,
                text = text,
            )
        }

        return transactionTask.await()
    }

    override suspend fun saveNote(note: Note) {
        val id = note.entry.id

        val entryDocRef = firestore
            .collection(NoteEntry.Path)
            .document(id)

        val contentDocRef = firestore
            .collection(NoteContent.Path)
            .document(id)

        val transactionTask = firestore.runTransaction { transaction ->
            val fsContentData = mapOf(NoteContent.Fields.Text to note.text)
            transaction.set(contentDocRef, fsContentData)

            val fsEntryData = FirestoreNoteEntry.toFirestore(note.entry) +
                    mapOf(NoteEntry.Fields.Content to contentDocRef)

            transaction.set(entryDocRef, fsEntryData)
        }

        transactionTask.await()
    }
}
