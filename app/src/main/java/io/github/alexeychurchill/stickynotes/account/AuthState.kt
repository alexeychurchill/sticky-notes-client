package io.github.alexeychurchill.stickynotes.account

import io.github.alexeychurchill.stickynotes.core.User

sealed interface AuthState {
    object None : AuthState

    data class Authorized(val user: User) : AuthState
}
