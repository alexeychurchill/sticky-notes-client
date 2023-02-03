package io.github.alexeychurchill.stickynotes.account.domain

import io.github.alexeychurchill.stickynotes.core.model.User

interface UserRepository {

    suspend fun currentUserSync()

    suspend fun searchUser(query: String): List<User>
}
