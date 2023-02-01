package io.github.alexeychurchill.stickynotes.account.domain

interface UserRepository {

    suspend fun currentUserSync()
}
