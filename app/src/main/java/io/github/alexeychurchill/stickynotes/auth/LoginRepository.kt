package io.github.alexeychurchill.stickynotes.auth

import io.github.alexeychurchill.stickynotes.core.User

interface LoginRepository {

    suspend fun login(login: String, password: String): User?
}
