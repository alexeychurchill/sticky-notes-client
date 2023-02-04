package io.github.alexeychurchill.stickynotes.core.extension

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun FirebaseUser.toUser(): User {
    return User(
        id = uid,
        login = email ?: "",
        firstName = displayName ?: "",
        lastName = "",
    )
}

fun Query.asSnapshotFlow(): Flow<QuerySnapshot> {
    return callbackFlow {
        val subscription = addSnapshotListener { value, error ->
            error?.let { throw it }
            value?.let { trySendBlocking(it) }
        }
        awaitClose { subscription.remove() }
    }
}
