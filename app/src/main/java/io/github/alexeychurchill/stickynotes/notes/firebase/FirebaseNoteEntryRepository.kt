@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.alexeychurchill.stickynotes.notes.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.FirestoreNoteEntry
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseNoteEntryRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dispatchers: DispatcherProvider,
    private val accountRepository: AccountRepository,
) : NoteEntryRepository {

    override val allNotes: Flow<List<NoteEntry>>
        get() = accountRepository
            .user
            .flatMapLatest { user ->
                flowOfUserNoteEntries(user.id)
            }
            .flowOn(dispatchers.io)

    override suspend fun getEntry(id: String): NoteEntry? {
        val docRef = firestore.collection(NOTE_ENTRIES_PATH)
            .document(id)
            .get()
            .await()
        return docRef.data?.let { data ->
            FirestoreNoteEntry.toDomain(docRef.id, data)
        }
    }

    override suspend fun create(entry: NoteEntry): NoteEntry {
        val docRef = firestore.collection(NOTE_ENTRIES_PATH)
            .add(FirestoreNoteEntry.toFirestore(entry))
            .await()
        return entry.copy(id = docRef.id)
    }

    override suspend fun delete(id: String) {
        firestore.collection(NOTE_ENTRIES_PATH)
            .document(id)
            .delete()
            .await()
    }

    private fun flowOfUserNoteEntries(ownerId: String): Flow<List<NoteEntry>> {
        return callbackFlow {
            val subscription = firestore
                .collection(NOTE_ENTRIES_PATH)
                .whereEqualTo("ownerId", ownerId)
                .addSnapshotListener { value, _ ->
                    val entries = toNoteEntries(value)
                    trySendBlocking(entries)
                }

            awaitClose {
                subscription.remove()
            }
        }
    }

    private fun toNoteEntries(value: QuerySnapshot?): List<NoteEntry> = value
        ?.documents
        ?.mapNotNull { snapshot ->
            snapshot.data?.let { data ->
                FirestoreNoteEntry.toDomain(snapshot.id, data)
            }
        }
        ?: emptyList()

    private companion object {
        const val NOTE_ENTRIES_PATH = "note_entries"
    }
}
