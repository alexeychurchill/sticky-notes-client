package io.github.alexeychurchill.stickynotes.account.domain

import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun currentUserSync()

    suspend fun contacts(): Flow<List<User>>

    suspend fun searchUser(query: String): List<User>

    suspend fun outgoingRequests(): Flow<List<User>>

    suspend fun ingoingRequests(): Flow<List<User>>

    suspend fun makeRequest(userId: String)

    suspend fun cancelRequest(userId: String)

    suspend fun acceptRequest(userId: String)

    suspend fun rejectRequest(userId: String)
}
