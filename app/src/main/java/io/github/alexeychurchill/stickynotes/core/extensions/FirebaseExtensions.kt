package io.github.alexeychurchill.stickynotes.core.extensions

import com.google.firebase.auth.FirebaseUser
import io.github.alexeychurchill.stickynotes.core.User

fun FirebaseUser.toUser(): User {
    return User(
        id = uid,
        login = email ?: "",
        firstName = displayName ?: "",
        lastName = "",
    )
}
