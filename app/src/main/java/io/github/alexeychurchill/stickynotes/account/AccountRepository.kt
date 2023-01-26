package io.github.alexeychurchill.stickynotes.account

import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val authState: Flow<AuthState>

    suspend fun login(login: String, password: String): User?

    suspend fun register(login: String, password: String): User?
}
