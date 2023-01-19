package io.github.alexeychurchill.stickynotes.account

import io.github.alexeychurchill.stickynotes.core.User

interface AccountRepository {

    suspend fun login(login: String, password: String): User?

    suspend fun register(login: String, password: String): User?
}
