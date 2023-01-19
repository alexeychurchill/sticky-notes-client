package io.github.alexeychurchill.stickynotes.account

import com.google.firebase.auth.FirebaseAuth
import io.github.alexeychurchill.stickynotes.core.User
import io.github.alexeychurchill.stickynotes.core.toUser
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class FirebaseAccountRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AccountRepository {

    override suspend fun login(login: String, password: String): User? {
        return firebaseAuth.signInWithEmailAndPassword(login, password)
            .asDeferred()
            .await()
            .user
            ?.toUser()
    }

    override suspend fun register(login: String, password: String): User? {
        return firebaseAuth.createUserWithEmailAndPassword(login, password)
            .asDeferred()
            .await()
            .user
            ?.toUser()
    }
}
