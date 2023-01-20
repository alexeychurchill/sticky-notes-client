@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.alexeychurchill.stickynotes.account

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import io.github.alexeychurchill.stickynotes.core.User
import io.github.alexeychurchill.stickynotes.core.toUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class FirebaseAccountRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AccountRepository {

    override val authState: Flow<AuthState>
        get() = callbackFlow {
            val authListener = AuthStateListener { auth ->
                val user = auth.currentUser?.toUser()
                val state = user?.let(AuthState::Authorized) ?: AuthState.None
                trySendBlocking(state)
            }
            firebaseAuth.addAuthStateListener(authListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authListener)
            }
        }

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
