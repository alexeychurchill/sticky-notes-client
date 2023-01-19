package io.github.alexeychurchill.stickynotes.core

import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User {
    return User(
        id = uid,
        login = email ?: "",
        firstName = displayName ?: "",
        lastName = "",
    )
}
