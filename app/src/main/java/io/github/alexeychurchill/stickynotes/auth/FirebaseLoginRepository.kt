package io.github.alexeychurchill.stickynotes.auth

import com.google.firebase.auth.FirebaseAuth
import io.github.alexeychurchill.stickynotes.core.User
import io.github.alexeychurchill.stickynotes.core.toUser
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class FirebaseLoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : LoginRepository {

    override suspend fun login(login: String, password: String): User? {
        return firebaseAuth.signInWithEmailAndPassword(login, password)
            .asDeferred()
            .await()
            .user
            ?.toUser()
    }
}
