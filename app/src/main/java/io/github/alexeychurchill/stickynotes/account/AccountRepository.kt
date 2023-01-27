package io.github.alexeychurchill.stickynotes.account

import io.github.alexeychurchill.stickynotes.account.AuthState.Authorized
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

interface AccountRepository {

    val authState: Flow<AuthState>

    val user: Flow<User>
        get() = authState
            .filterIsInstance<Authorized>()
            .map { it.user }

    suspend fun login(login: String, password: String): User?

    suspend fun register(login: String, password: String): User?
}
