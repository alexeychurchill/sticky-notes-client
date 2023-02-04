package io.github.alexeychurchill.stickynotes.account.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.ContactRequest
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Fields
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Path
import io.github.alexeychurchill.stickynotes.core.data.FirestoreUser
import io.github.alexeychurchill.stickynotes.core.extension.asSnapshotFlow
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val firestore: FirebaseFirestore,
    private val accountRepository: AccountRepository,
) : UserRepository {

    override suspend fun currentUserSync() = withContext(dispatchers.default) {
        accountRepository.user.collectLatest { user ->
            val fsUser = FirestoreUser.toFirestore(user)
            val docRef = firestore.collection(Path).document(user.id)
            docRef.set(fsUser).await()
        }
    }

    // TODO: Think regarding username, firstName, lastName FTS search
    // implementation using ElasticSearch, Algolia etc.
    override suspend fun searchUser(query: String): List<User> {
        val currentUserId = accountRepository.user.first().id
        return searchByAsync(Fields.Username, query)
            .await()
            .distinctBy(User::id)
            .filterNot { it.id == currentUserId }
    }

    override suspend fun outgoingRequests(): Flow<List<User>> {
        return contactRequests(
            fieldById = ContactRequest.Fields.From,
            fieldGetUser = ContactRequest.Fields.To,
        )
    }

    override suspend fun ingoingRequests(): Flow<List<User>> {
        return contactRequests(
            fieldById = ContactRequest.Fields.To,
            fieldGetUser = ContactRequest.Fields.From,
        )
    }

    override suspend fun makeRequest(userId: String) {
        val currentUserId = accountRepository.user.first().id
        val data = mapOf(
            ContactRequest.Fields.From to currentUserId,
            ContactRequest.Fields.To to userId,
        )
        firestore
            .collection(ContactRequest.Path)
            .document()
            .set(data)
            .await()
    }

    private suspend fun getUser(userId: String): User? {
        val snapshot = firestore
            .collection(Path)
            .document(userId)
            .get()
            .await()
        return snapshot.data?.let {
            FirestoreUser.toDomain(snapshot.id, it)
        }
    }

    private suspend fun searchByAsync(field: String, query: String): Deferred<List<User>> {
        return CoroutineScope(dispatchers.io).async {
            firestore.collection(Path)
                .whereGreaterThanOrEqualTo(field, query)
                .whereLessThanOrEqualTo(field, query + '\uF8FF')
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.data?.let { map ->
                        FirestoreUser.toDomain(snapshot.id, map)
                    }
                }
        }
    }

    private suspend fun contactRequests(
        fieldById: String,
        fieldGetUser: String,
    ): Flow<List<User>> {
        val currentUserId = accountRepository.user.first().id
        return firestore
            .collection(ContactRequest.Path)
            .whereEqualTo(fieldById, currentUserId)
            .asSnapshotFlow()
            .map { querySnapshot ->
                querySnapshot
                    .documents
                    .mapNotNull { snapshot ->
                        snapshot.data?.get(fieldGetUser) as String
                    }
                    .mapNotNull { userId ->
                        getUser(userId)
                    }
            }
    }
}
