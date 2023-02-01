package io.github.alexeychurchill.stickynotes.account.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Path
import io.github.alexeychurchill.stickynotes.core.data.FirestoreUser
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
}
