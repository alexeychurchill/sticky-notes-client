package io.github.alexeychurchill.stickynotes.account.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Fields
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Path
import io.github.alexeychurchill.stickynotes.core.data.FirestoreUser
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
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
        return searchByAsync(Fields.Username, query)
            .await()
            .distinctBy(User::id)
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
}
