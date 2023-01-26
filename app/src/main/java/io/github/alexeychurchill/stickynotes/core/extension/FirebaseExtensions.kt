package io.github.alexeychurchill.stickynotes.core.extension

import com.google.firebase.auth.FirebaseUser
import io.github.alexeychurchill.stickynotes.core.model.User

fun FirebaseUser.toUser(): User {
    return User(
        id = uid,
        login = email ?: "",
        firstName = displayName ?: "",
        lastName = "",
    )
}
